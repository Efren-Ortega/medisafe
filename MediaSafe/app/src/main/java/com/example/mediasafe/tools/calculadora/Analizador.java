package com.example.mediasafe.tools.calculadora;

import java.util.Stack;
import java.util.regex.Pattern;

public class Analizador {
    private String expresion;
    private Stack<String> stackOperadores = new Stack<String>();
    private Stack<Float> stackOperandos =  new Stack<Float>();

    public void Analizador(String exp){
        this.expresion = exp.trim();
    }

    public void setExpresion(String expresion) {
        this.expresion = expresion;
    }

    public float evaluar(){
        for (int i=0; i< this.expresion.length(); i++){
            char caracterActual = this.expresion.charAt(i);

            //Identificar si el CaracterActual es un Operador -> (), +, -, /, *
            String operadoresDelimitadores = "[\\(|\\)|\\+|\\-|\\/|\\*|\\^|\\.]";
            if(Pattern.matches(operadoresDelimitadores, new Character(caracterActual).toString())){

                //Validar que el stackOperadores no este vacio
                if(this.stackOperadores.size()!=0){

                    //Identificar si el primer elemento del stackOperadores no son parentesis -> ()
                    String soloOperadores = "[\\.|\\+|\\-|\\/|\\*|\\^]";
                    if(Pattern.matches(soloOperadores, this.stackOperadores.peek()) && Pattern.matches(soloOperadores, new Character(caracterActual).toString())){

                        //Validar la Jerarquía -> True Debemos operar && False No debemos Operar
                        if(this.evaluarJerarquía(new Character(caracterActual).toString())){
                            //Operamos llamando a la función operar() y el resultado se almacena en el stackOperandos
                            this.stackOperandos.push(this.operar());
                        }

                    }
                }

                String numeros ="[0-9]";
                if(i>0){
                    if(caracterActual == '(' && Pattern.matches(numeros,new Character(this.expresion.charAt(i-1)).toString())){
                        System.out.println(new Character(this.expresion.charAt(i-1)));
                        this.stackOperadores.push("*");
                    }
                }

                //Agregar el actual caracter al stackOperador que siempre sera un Operador por eso se valida en la linea 16
                this.stackOperadores.push(new Character(caracterActual).toString());

                //Si el caracter Actual es un Paretesis de Cierre significa que ya paso uno de apertura por ende debemos
                //operar hasta encontrar el parentesis de apertura más cercano.
                if(new Character(caracterActual).toString().equals(")")){
                    this.stackOperadores.pop();//Eliminamos el parentesis

                    //Validamos que en el stack operadores haiga operadores (+,-,/,*,^) de lo contrario
                    //entrariamos en un blucle infinito
                    String soloOperadores = "^[\\.|\\+|\\-|\\/|\\*|\\^]$";
                    if(Pattern.matches(soloOperadores, this.stackOperadores.peek())){
                        this.stackOperandos.push(this.operar());
                    }

                    //Este blucle permite hacer los calculos hasta que encuentre un Parentesis de Apertura
                    while(true){
                        if(this.stackOperadores.peek().equals("(")){
                            this.stackOperadores.pop();//Elimina el parentesis encontrado
                            break;
                        }else{
                            //Operamos llamando a la función operar() y el resultado se almacena en el stackOperandos
                            this.stackOperandos.push(this.operar());
                        }
                    }

                }
            }else{
                //Es este else el caracterActual es siempre numero (Operando) o punto decimal.

                //En este if en caso de que se cumplan sus condiciones nos permitira poder manejar numeros de más de una cifra
                if(i>0 && caracterActual != '.'){
                    String numeros ="^[0-9]$";
                    //Validamos si el elemento anterior al caracterActual es un numero
                    if(Pattern.matches(numeros,new Character(this.expresion.charAt(i-1)).toString())){
                        //Obtenemos el primer numero del stackOperandos()
                        String first = Integer.toString(Math.round(this.stackOperandos.pop()));
                        //Convertirmos el caracter actual a un string para concatenar
                        String caracterActualString = new Character(caracterActual).toString();

                        //Concatenamos el primer numero con el caracterActualString y lo convertimos a Float y esto se almacena en el stackOperandos()
                        this.stackOperandos.push(Float.parseFloat(first.concat(caracterActualString)));
                    }else{
                        //En caso contrario solo agregamos el caracterActual en Flotante al stackOperandos()
                        this.stackOperandos.push(Float.parseFloat(new Character(caracterActual).toString()));
                    }
                }else{
                    //En caso contrario solo agregamos el caracterActual en Flotante al stackOperandos()
                    this.stackOperandos.push(Float.parseFloat(new Character(caracterActual).toString()));
                }

            }

        }

        //Ahora en la mayoría de casos se terminara de iterar nuestra Expresión Aritmetica pero aun no se habra
        //conseguido el Resultados esperado esto porque aun hay operadores en el stackOperadores

        //Entonces valido si mi stackOperadores es diferente de vacio entonces recorro este stackOperadores
        if(this.stackOperadores.size() != 0){

            int size = this.stackOperadores.size();
            for(int i=0; i<size; i++){ //Termina hasta recorrer _todo el stackOperadores
                //Operamos llamando a la función operar() y el resultado se almacena en el stackOperandos
                this.stackOperandos.push(this.operar());
            }
        }

        //Finalmente ya que cada vez que realizo un calculo lo almaceno en el stackOperandos para usarla en
        //posteriores calculos, cuando se llegue a esta linea el stackOperandos solo tendra un valor el cual sera
        //el Resultado total de la expresión, entonces solo devuelvo el unico valor de este stack con peek().
        return this.stackOperandos.peek();
    }


    public boolean evaluarJerarquía(String caracterActual){
        //Validamos si el operador en la cima de la PILA es menor al Operador Actual
        if(this.jerarquiaAritmetica(this.stackOperadores.peek()) < this.jerarquiaAritmetica(caracterActual)){
            return false;//Si, si, No Realizamos Operación Aritemetica
        }

        return true;//Si, no, Debemos operar los datos de los stacks
    }

    //Función que retorna una valor según el grado jerarquico del Operador siendo:
    //(+ & -) = 0 los más bajos y (.) = 3 el más alto
    public int jerarquiaAritmetica(String caracterActual){
        switch (caracterActual){
            case "+":
                return 0;

            case "-":
                return 0;

            case "*":
                return 1;

            case "/":
                return 1;

            case "^":
                return 2;

            case ".":
                return 3;

            default:
                return -1;
        }
    }

    public float operar(){
        //Asigno mis 3 variables operando1 operador operando2 -> x +|-|/|*|^ y
        //Uso el método pop() ya que cada vez que uso un valor de stackOperandos y stackOperadores debo
        //eliminarlos de esos stacks ya que se vuelven obsoletos porque ya serán calculados.
        float operando2 = this.stackOperandos.pop();
        String operador = this.stackOperadores.pop();
        float operando1 = this.stackOperandos.pop();

        float result = 0;

        //Realizo la operación según el Operador que corresponda
        switch (operador){
            case "+":
                result = operando1 + operando2;
                break;

            case "-":
                result = operando1 - operando2;
                break;

            case "*":
                result = operando1 * operando2;
                break;

            case "/":
                result = operando1 / operando2;
                break;

            case "^":
                result = (float) (Math.pow(operando1,operando2));
                break;

            case ".":
                //Aquí manejo los puntos decimales como si fuera un operador

                //Convierto operando 1 y 2 a String
                String operando_1_str = Integer.toString(Math.round(operando1));
                String operando_2_str =Integer.toString(Math.round(operando2));

                //Concateno operado1 + operador + operando2 y los convierto a Flotante y a su vez lo almaceno en la variable result para retornarlo
                //y se agregue al stackOperandos
                result = Float.parseFloat(operando_1_str + operador + operando_2_str);
        }

        //Devuelvo el resultado de la operación aritmetica
        return result;
    }

}