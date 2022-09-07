import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.*;

/*
КС-грамматика имеет вид G = ( N , ∑ , P , S ), где
                N - алфавит нетерминальных символов (может состоять из всего, кроме {',\,-,>,} )
                ∑ - алфавит терминальных символов (может состоять из всего, кроме N⋃{',\,-,>,} )
                P - конечное множество правил
                    Правила могут иметь вид A->a , где A∈N, a∈(N⋃∑)*
                S - выделенный символ из N, называемый начальным символом
                e - зарезервированный символ для пустого слова, он может появиться только в правилах!

Все символы и правила разделяются запятыми, БЕЗ ПРОБЕЛОВ!

Пример 1 КС, является LL1:
N = S,B
∑ = 0,1,2
P = S->B2,B->0,B->1
S = S

Пример 2 КС, является LL1:
N = S,A
∑ = a,b
P = S->aAS,S->b,A->a,A->bSA
S = S

Пример 3 КС, НЕ является LL1
N = S
∑ = a,b
P = S->a,S->ab
S = S

Пример 4 КС, НЕ является LL1
N = S,A
∑ = a,b
P = S->aA,A->ab,S->a
S = S
 */
public class Main {

    private static Scanner scanIn = new Scanner(System.in);

    private static HashSet<Character> N = new HashSet<Character>();
    private static HashSet<Character> T = new HashSet<Character>();
    private static Multimap<Character, char[]> P = ArrayListMultimap.create();
    private static Character S;

    public static void main(String[] args) {
        System.out.println("Введите Контекстно свободную грамматику:");

        parseN();
        parseT();
        parseP();
        parseS();

        Grammar grammar = new Grammar(N,T,P,S);

        boolean result = grammar.checkIfLL1();
        System.out.println("\n____________________________________________");
        System.out.println(result ? "Эта грамматика LL(1)" : "Эта грамматика не LL(1)");

    }

    private static void parseN(){
        String str;
        while (true){
            boolean flag = false;
            System.out.print("N = ");
            str = scanIn.nextLine();
            String newStr = str.replaceAll("[^->e'\"]", "");
            if ( 0 < newStr.length()) {
                System.out.println("Использован неверный символ, попробуйте еще раз");
            }
            else{
                String[] N_arr = str.split(",");
                for (String value : N_arr) {
                    char[] arr = value.toCharArray();
                    if(arr.length>1){
                        System.out.println("Нельзя использовать связку символов за один, попробуйте еще раз");
                        flag = true;
                        N.clear();
                        break;
                    }
                    else {
                        N.add(arr[0]);
                    }
                }

                if(!flag){
                    break;
                }
            }
        }
    }

    public static void parseT(){
        String str;
        while (true){
            boolean flag = false;
            boolean flag1 = false;
            System.out.print("∑ = ");
            str = scanIn.nextLine();
            String newStr = str.replaceAll("[^->e'\"]", "");
            if ( 0 < newStr.length()) {
                System.out.println("Использован неверный символ, попробуйте еще раз");
            }
            else{

                for (Character c:str.toCharArray()) {
                    if(N.contains(c)){
                        System.out.println("Использован символ "+c+" из множества N, попробуйте еще раз");
                        flag=true;
                        break;
                    }
                }

                if(flag){
                    continue;
                }
                String[] Terminal_arr = str.split(",");
                for (String value : Terminal_arr) {
                    char[] arr = value.toCharArray();
                    if(arr.length>1){
                        System.out.println("Нельзя использовать связку символов за один, попробуйте еще раз");
                        flag1 = true;
                        T.clear();
                        break;
                    }
                    else {
                        T.add(arr[0]);
                    }
                }
                if(!flag1){
                    break;
                }
            }
        }
    }

    public static void parseP(){
        String str;
        while (true){
            boolean flag = false;
            System.out.print("P = ");
            str = scanIn.nextLine();

            if(!str.matches("[^'\"]*")){
                System.out.println("Использован неверный символ, попробуйте еще раз");
                continue;
            }

            //[ a->b , c->d ]
            String[] P_arr = str.split(",");

            for (String s:P_arr) {//a->bcd

                String[] P_current = s.split("->");//[ a , bcd ]
                if(P_current.length == 2){
                    char[] A = P_current[0].toCharArray(); // [ a ]
                    if(A.length == 1 && N.contains(A[0])){
                        char[] a = P_current[1].toCharArray();// [ b, c, d ]
                        for (char c:a) {
                            if ((!N.contains(c) && !T.contains(c)) && c != 'e') {
                                System.out.println("Использовался символ не из множеств, попробуйте еще раз");
                                flag = true;
                                break;
                            }

                        }
                        if(flag){
                            break;
                        }
                        else{
                            P.put(A[0],a);
                        }
                    }
                    else {
                        System.out.println("Нетерминал не принадлежит множеству N, попробуйте еще раз");
                        flag = true;
                        break;
                    }
                }
                else {
                    System.out.println("Правила должны иметь вид А->a, попробуйте еще раз");
                    flag = true;
                    break;
                }

            }

            if(!flag){
                break;
            }
        }
    }

    public static void parseS(){
        String str;
        while (true){
            System.out.print("S = ");
            str = scanIn.nextLine();
            char[] chars = str.toCharArray();
            if(chars.length !=1){
                System.out.println("Нужен лишь один нетерминал, попробуйте еще раз");
            }
            else if(!N.contains(chars[0])){
                System.out.println("Нетерминал не содержится в множестве N, попробуйте еще раз");
            }
            else {
                S=chars[0];
                break;
            }
        }
    }

}