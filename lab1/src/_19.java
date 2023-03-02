public class _19{
    public static boolean scan(String s){
        int state = 0;
        int i = 0;
        int length = s.length();

        while(state!=-1 && i<length){
            final char c = s.charAt(i++);
            
            switch(state){
            case 0:
            	if(c=='/')
            		state=1;
            	else if(c=='a')
            		state=5;
            	else if(c=='*')
            		state=5;
            	else
            		state=-1;
            	break;
            case 1:
            	if(c=='*')
            		state=2;
            	else if(c=='a')
            		state=5;
            	else if(c=='/')
            		state=5;
            	else
            		state=-1;
            	break;
            case 2:
            	if(c=='/')
            	{}
            	else if(c=='a')
            	{}
            	else if(c=='*')
            		state=3;
            	else
            		state=-1;
            	break;
            case 3:
            	if(c=='/')
            		state = 4;
            	else if(c=='a')
            		state = 2;
            	else if(c=='*')
            	{}
            	else
            		state=-1;
            	break;
            case 4:
            	if(c=='/'||c=='*'||c=='a')
            		state=5;
            	else
            		state=-1;
            	break;
            
            case 5:
            	if(c=='/'||c=='*'||c=='a')
            		state=5;
            	else
            		state=-1;
            	break;
            }
        }
        return state == 4;
    }

    public static void main(String[] args){
        String[] correctStrings={"/****/", "/*a*a*/", "/*a/**/", "/**a///a/a**/", "/**/","/*/*/"};
        String[] wrongStrings={"/*/", "/**/***/"};
        for(String s:correctStrings)
            if(!scan(s))
            {
                System.out.println("Stringa corretta non riconosciuta: "+s);   
            }else
            	System.out.println("YES");
        for(String s:wrongStrings)
            if(scan(s))
            {
                System.out.println("Stringa erroneamente riconosciuta: "+s);   
            }else
            	System.out.println("YES");
    }

}