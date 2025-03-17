package external;

import com.intuit.karate.junit5.Karate;

class ExternalRunner {
    
    @Karate.Test
    Karate testLogin() {
        return Karate.run("login").relativeTo(getClass());
    }    

    @Karate.Test
    Karate testWs() {
        return Karate.run("ws").relativeTo(getClass());
    }  

    @Karate.Test
    Karate test1() {
        return Karate.run("test1").relativeTo(getClass());
    }  

    @Karate.Test
    Karate test2() {
        return Karate.run("test2").relativeTo(getClass());
    }  
}
