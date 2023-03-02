public class _110{
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
	            	{}
	            	else if(c=='*')
	            	{}
	            	else
	            		state=-1;
	            	break;
	            case 1:
	            	if(c=='*')
	            		state=2;
	            	else if(c=='a')
	            		state=0;
	            	else if(c=='/')
	            	{}
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
	            		state = 0;
	            	else if(c=='a')
	            		state = 2;
	            	else if(c=='*')
	            	{}
	            	else
	            		state=-1;
	            	break;
	            
            }
        }
        return (state==0 || state==1);
    }

    public static void main(String[] args){
        String[] correctStrings={"aaa/****/aa", "aa/*a*a*/", "aaaa", "/****/", "/*aa*/","*/a", "a/**/***a", "a/**/***/a", "a/**/aa/***/a"};
        String[] wrongStrings={"aaa/*/aa", "a/**//***a", "aa/*aa"};
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
