public class _17{
	public static boolean scan(String s){ /* alfabeto= UTF-8 */
		/*Se serve la differenza tra uppercase e lowercase, basta togliere le due funzioni toLowerCase() */
        String name="Carmine";
        name=name.toLowerCase();
        int nameLength = 7;
        
        int state = 0;
        int i = 0;
        int length = s.length();

        boolean result; 

        while(i<length){
        	char c=0;
        	if(i<nameLength)
        		c= Character.toLowerCase(s.charAt(i)); //doesn't affect Character if it's a number
            
        	switch(state) {
        		case 0:
        		case 1:
        		case 2:
        		case 3:
        		case 4:
        		case 5:
        		case 6:
                	if(c==name.charAt(i))
                		state++;
                	else
                		state=state+1+7;        			
        			break;
        		case 8:
        		case 9:
        		case 10:
        		case 11:
        		case 12:
        		case 13:
                	if(c==name.charAt(i))
                		state++;
                	else
                		state=15;
        			break;
        		case 7:
        		case 14:
        			state=15;
        			break;
        	}
        	
            i++;
        }

        result=(state == 7)||(state==14);
        return result;
    }

    public static void main(String[] args){
        String[] correctStrings={"Carmine","Cvrmine","oarmine","Ca%mine","Carmino","Carmene"};
        String[] wrongStrings={"Eva","Perro","Cadmvne","$arm!ne","Carrrne","AAAAAAAAAA"};
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