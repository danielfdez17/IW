
let currentSort = {
    column: null,
    direction: 'asc' // 'asc' o 'desc'
};

function sortTable(columnIndex) {
    const table = document.getElementById('mainTable');
    const tbody = table.querySelector('tbody');
    const rows = Array.from(tbody.querySelectorAll('tr'));
    const header = table.querySelectorAll('th')[columnIndex];
    
    // Resetear headers
    table.querySelectorAll('th').forEach(h => h.classList.remove('active'));
    
    // Direccion
    if (currentSort.column === columnIndex) {
        currentSort.direction = currentSort.direction === 'asc' ? 'desc' : 'asc';
    } else {
        currentSort.direction = 'asc';
    }
    
    currentSort.column = columnIndex;
    header.classList.add('active');

    // Actualizar iconos
    document.querySelectorAll('.sort-icon').forEach(icon => icon.style.display = 'none');
    const icons = header.querySelectorAll('.sort-icon');
    if(icons.length > 0){
        const activeIcon = currentSort.direction === 'asc' ? icons[0] : icons[1];
        activeIcon.style.display = 'inline-block';
    }

    // Ordenar
    rows.sort((a, b) => {
        const aValue = a.cells[columnIndex].textContent;
        const bValue = b.cells[columnIndex].textContent;
        
        let comparator;
        
        // Ver cual es el tipo de dato de la columna
        switch(columnIndex) {
            case 0: // ID (numerico)
                comparator = parseInt(aValue) - parseInt(bValue);
                break;
            case 3: // Precio (los '.' son separadores y las ',' decimales)
                const numA = parseFloat(aValue.replace(/\$/g, '').replace(/\./g, '').replace(',', '.'));
                const numB = parseFloat(bValue.replace(/\$/g, '').replace(/\./g, '').replace(',', '.'));
                comparator = numA - numB;
                break;
            case 5: // Fecha (dd/mm/yyyy)
                comparator = compareDates(aValue, bValue);
                break;
            case 2: case 4: // Texto
                comparator = aValue.localeCompare(bValue);

            default:
                comparator = aValue.localeCompare(bValue);
        }
        
        return currentSort.direction === 'asc' ? comparator : -comparator;
    });

    // Reinsertar filas ordenadas
    tbody.append(...rows);
}

function compareDates(a, b) {
    const parseDate = (str) => {
        const [day, month, year] = str.split('/');
        return new Date(year, month - 1, day);
    };
    
    const dateA = parseDate(a);
    const dateB = parseDate(b);
    return dateA - dateB;
}
