package es.ucm.fdi.iw.business.enums;

public enum EstadoSubasta {
    PENDIENTE,      // Todavía no ha comenzado la subasta, se cambiara a estado en curso con el Scheduled
    EN_CURSO,       // Subasta en curso, se cambiara a estado finalizada con el Scheduled
    FINALIZADA,     
    CANCELADA;       // Estado de subasta cancelada, este estado solo lo puede hacer el admin o el creador de la subasta
    public static String getTxt(EstadoSubasta estadoSubasta){
        switch (estadoSubasta) {
            case PENDIENTE:
                return "Pendiente";
            case EN_CURSO:
                return "En curso";
            case FINALIZADA:
                return "Finalizada";
            case CANCELADA:
                return "Cancelada";
            default:
                return null;
        }
    } 
}
