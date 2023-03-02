public class _14{
    public static boolean scan(String s){
        int state = 0;
        int i = 0;
        int length = s.length();
        boolean result; 

        while(state!=-1 && i<length){
            final char c = s.charAt(i++);
            
            switch(state){
                case 0:
                    if(Character.isLetter(c)){
                        state = 6;
                    }
                    else if(Character.isDigit(c)){
                        if((c-'0')%2==0)
                            state = 1;
                        else state = 2;
                     }
                    else if(c==' '){}
                    else{ state = -1; }
                    break;
                case 1:
                	if(Character.isLowerCase(c))
                		state = 6;
                	else if(Character.isUpperCase(c)){
                        if(c>='A'&&c<='K')
                            state=5;
                        else if(c>='L'&&c<='Z')
                            state=6;
                    }
                    else if(Character.isDigit(c)){ 
                        if((c-'0')%2==1)
                            state = 2;
                    }
                    else if(c==' '){ state = 3; }
                    else{ state = -1; }
                    break;
                case 2:
                	if(Character.isLowerCase(c))
                		state = 6;
                	else if(Character.isUpperCase(c)){
                        if(c>='A'&&c<='K')
                            state=6;
                        else if(c>='L'&&c<='Z')
                            state=5;
                    }
                    else if(Character.isDigit(c)){ 
                        if((c-'0')%2==0)
                            state = 1;
                    }
                    else if(c==' '){ state = 4; }
                    else{ state = -1; }
                    break;
                case 3:
                	if(Character.isLowerCase(c))
                		state = 6;
                	else if(Character.isUpperCase(c)){
                        if(c>='A'&&c<='K')
                            state=5;
                        else if(c>='L'&&c<='Z')
                            state=6;
                    }
                    else if(Character.isDigit(c)){ state = 6; }
                    else if(c==' '){}
                    else{ state = -1; }
                    break;
                case 4:
                	if(Character.isLowerCase(c))
                		state = 6;
                	else if(Character.isUpperCase(c)){
                        if(c>='A'&&c<='K')
                            state=6;
                        else if(c>='L'&&c<='Z')
                            state=5;
                    }
                    else if(Character.isDigit(c)){ state = 6; }
                    else if(c==' '){}
                    else{ state = -1; }                
                    break;
                case 5: 
                    if(Character.isLowerCase(c)){}
                    else if(Character.isDigit(c) || Character.isUpperCase(c)){ state=6; }
                    else if(c==' '){state=7;}
                    else{ state = -1; }
                    break;
                case 6: //stato pozzo
                    if(Character.isLetter(c)){}
                    else if(Character.isDigit(c)){}
                    else if(c==' '){}
                    else{ state = -1; }
                    break;
                case 7: 
                    if(Character.isUpperCase(c)){ state = 5;}
                    else if(Character.isDigit(c) || Character.isLowerCase(c)){state = 6;}
                    else if(c==' '){}
                    else{ state = -1; }
                    break;
            }
        }
        result = (state == 5) || (state == 7);
        return result;
    }

    public static void main(String[] args){
        String[] correctStrings={"123456De Laurentis  ","  654321Rossi  ","2 Bianchi","122B","123456De Gasperi","654321 Rossi","  123456 Bianchi   "};
        String[] wrongStrings={"65 4321Bianchi","654321ro ssi","  12 3456Rossi","654322"," Rossi","1234 56Bianchi "};
        for(String s:correctStrings)
            if(!scan(s))
            {
                System.out.println("Stringa corretta non riconosciuta: '"+s+"'");   
            }else
            	System.out.println("YES");
        for(String s:wrongStrings)
            if(scan(s))
            {
                System.out.println("Stringa erroneamente riconosciuta: '"+s+"'");   
            }else
            	System.out.println("YES");
    }

}