/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;

import static Principal.CDI.Exp;
import java.util.ArrayList;

/**
 *
 * @author Alexander Batista
 */
public class ProcesarFunciones extends CDI {
    
    public static ExpresionAlgebraica[] jeraquia(ArrayList deriv, ArrayList signo) {
        int s = 0;
        Boolean letra = false, numero = false, potencia = false;
        String le;
        ArrayList orden = new ArrayList();
        
        if (signo.size() == deriv.size()) {
            orden.add(signo.get(0));
        }
        for (int i = 0; i < deriv.size(); i++) {
            orden.add(deriv.get(i));
            if (i < signo.size()) {
                orden.add(signo.get(i));
            }
        }
        
        String[] exponentes = new String[deriv.size()];
        
        for (Object orden1 : orden) {
            for (int j = 0; j < orden1.toString().length(); j++) {
                if (orden1.toString().charAt(j) == '^') {
                    potencia = true;
                    // if (true) 
                    {
                        exponentes[s] = orden1.toString().substring(j + 1, orden1.toString().length());
                    }
                    
                    s++;
                    //t = j;
                } else if (!potencia) {
                    le = String.valueOf(orden1.toString().charAt(j));
                    if ((le.hashCode() >= 97 && le.hashCode() <= 122) || (le.hashCode() >= 65 && le.hashCode() <= 90)) {
                        letra = true;
                        numero = false;
                    } else if ((le.hashCode() >= 48 && le.hashCode() <= 57)) {
                        letra = false;
                        numero = true;
                    }
                }
                if (potencia) {
                    letra = false;
                    numero = false;
                }
            }
            if (letra) {
                exponentes[s] = "1";
                s++;
            } else if (numero) {
                exponentes[s] = "0";
                s++;
            }
            potencia = false;
            letra = false;
            numero = false;
        }
        exponentes = dividircad.correr(exponentes);
        return Ordenar(exponentes, orden);
    }
    
    public static ExpresionAlgebraica[] Ordenar(String[] exponentes, ArrayList orden) {
        Boolean letra = false, numero = false, potencia = false;
        String le;
        ArrayList Completo = new ArrayList();
        ArrayList Terminos = new ArrayList();
        if (orden.size() == 1 && exponentes.length == 1 && "1".equals(exponentes[0])) {
            if (orden.get(0).toString().charAt(0) == '-') {
                Completo.add("-" + orden.get(0) + "^" + exponentes[0]);
            } else {
                Completo.add("+" + orden.get(0) + "^" + exponentes[0]);
            }
            
        } else {
            for (String exponente : exponentes) {
                for (int i = 0; i < orden.size(); i++) {
                    for (int j = 0; j < orden.get(i).toString().length(); j++) {
                        if (orden.get(i).toString().charAt(j) == '^') {
                            potencia = true;
                            if (exponente.equals(orden.get(i).toString().substring(j + 1, orden.get(i).toString().length()))) {
                                if (i != 0) {
                                    Completo.add(orden.get(i - 1).toString());
                                    Completo.add(orden.get(i).toString());
                                    orden.set(i - 1, "*");
                                    orden.set(i, "*");
                                } else if (i == 0) {
                                    Completo.add("+");
                                    Completo.add(orden.get(i).toString());
                                    orden.set(i, "*");
                                } else {
                                    Completo.add(orden.get(i).toString());
                                    orden.set(i, "*");
                                }
                            }
                        } else if (!potencia && Double.valueOf(exponente) <= 1) {
                            le = String.valueOf(orden.get(i).toString().charAt(j));
                            if ((le.hashCode() >= 97 && le.hashCode() <= 122) || (le.hashCode() >= 65 && le.hashCode() <= 90)) {
                                letra = true;
                                numero = false;
                            } else if ((le.hashCode() >= 48 && le.hashCode() <= 57)) {
                                letra = false;
                                numero = true;
                            }
                        }
                        if (potencia) {
                            letra = false;
                            numero = false;
                        }
                    }
                    if (letra && Integer.valueOf(exponente) == 1) {
                        if (i != 0) {
                            Completo.add(orden.get(i - 1).toString());
                            Completo.add(orden.get(i).toString());
                            orden.set(i - 1, '*');
                            orden.set(i, '*');
                        } else if (i == 0) {
                            Completo.add("+");
                            Completo.add(orden.get(i).toString());
                            orden.set(i, '*');
                        } else {
                            Completo.add(orden.get(i).toString());
                            orden.set(i, '*');
                        }
                    } else if (numero && Integer.valueOf(exponente) == 0) {
                        if (i != 0) {
                            Completo.add(orden.get(i - 1).toString());
                            Completo.add(orden.get(i).toString());
                            orden.set(i - 1, '*');
                            orden.set(i, '*');
                        } else if (i == 0) {
                            Completo.add('*');
                            Completo.add(orden.get(i).toString());
                            orden.set(i, '*');
                        } else {
                            Completo.add(orden.get(i).toString());
                            orden.set(i, '*');
                        }
                    }
                    potencia = false;
                    letra = false;
                    numero = false;
                }
            }
        }
        Completo.stream().filter((Completo1) -> (!"*".equals(Completo1.toString()))).forEach((Completo1) -> {
            Terminos.add(Completo1.toString());
        });
        Completo.clear();
        return llenar_exp(Terminos);
    }
    
    public static ExpresionAlgebraica[] llenar_exp(ArrayList Completo) {
        int x = 0, s = 0, p = 0;
        int cont = 0;
        for (int i = 0; i < Completo.size(); i++) {
            for (int j = 0; j < Completo.get(i).toString().length(); j++) {
                if (Completo.get(i).toString().charAt(j) == '^') {
                    cont++;
                }
            }
            if (cont > 1) {
                if (!Completo.get(i).equals("+") && !Completo.get(i).equals("-")) {
                    Completo.set(i, Completo.get(i).toString().substring(0, Completo.get(i).toString().lastIndexOf("^")));
                } else{
                    Completo.set(i, Completo.get(i).toString().substring(0,Completo.get(i).toString().length()));
                }
            }
        }
        float coef = 0;
        Boolean letra = false, numero = false, potencia = false;
        if (Completo.size() == 1) {
            Exp = new ExpresionAlgebraica[1];
        } else {
            Exp = new ExpresionAlgebraica[(Completo.size() / 2)];
        }
        String le = "", sign;
        sign = "";
        String exp = "", q, cadena = "";
        for (int i = 0; i < Completo.size(); i++) {
            for (int j = 0; j < Completo.get(i).toString().length(); j++) {
                if (Completo.get(i).toString().charAt(j) != '+' && Completo.get(i).toString().charAt(j) != '-') {
                    
                    if (Completo.get(i).toString().charAt(j) == '^') {
                        potencia = true;
                        exp = String.valueOf(Completo.get(i).toString().substring(Completo.get(i).toString().indexOf("^") + 1, Completo.get(i).toString().length()));
                        le = String.valueOf(Completo.get(i).toString().substring(Completo.get(i).toString().indexOf("^") - 1, Completo.get(i).toString().indexOf("^")));
                        if ("+".equals(Completo.get(i).toString().substring(0, j - 1)) || "".equals(Completo.get(i).toString().substring(0, j - 1))) {
                            coef = +1;
                        } else {
                            coef = Float.valueOf(Completo.get(i).toString().substring(0, j - 1));
                        }
                        if (i != 0) {
                            sign = Completo.get(i - 1).toString();
                        }
                    } else if (!potencia) {
                        q = String.valueOf(Completo.get(i).toString().charAt(j));
                        if ((q.hashCode() >= 97 && q.hashCode() <= 122) || (q.hashCode() >= 65 && q.hashCode() <= 90)) {
                            letra = true;
                            le = q;
                            if (i != 0) {
                                sign = Completo.get(i - 1).toString();
                            }
                            numero = false;
                        } else if ((q.hashCode() >= 48 && q.hashCode() <= 57)) {
                            letra = false;
                            numero = true;
                        }
                    }
                    if (potencia) {
                        letra = false;
                        numero = false;
                    }
                }
            }
            if (letra) {
                exp = "1";
                le = String.valueOf(Completo.get(i).toString().charAt(Completo.get(i).toString().length() - 1));
                if (Completo.get(i).toString().length() != 1) {
                    coef = Float.valueOf(Completo.get(i).toString().substring(0, Completo.get(i).toString().length() - 1));
                } else {
                    coef = 1;
                }
                if (i != 0) {
                    sign = Completo.get(i - 1).toString();
                }
            } else if (numero) {
                exp = "0";
                coef = Float.valueOf(Completo.get(i).toString());
                le = "";
                if (i != 0) {
                    sign = Completo.get(i - 1).toString();
                }
            }
            if (potencia || numero || letra) {
                Exp[x] = new ExpresionAlgebraica(sign, coef, le, exp);
                x++;
            }
            potencia = false;
            letra = false;
            numero = false;
            exp = "";
        }
        
        for (Object Completo1 : Completo) {
            cadena = cadena.concat(Completo1.toString());
        }
        if ("+".equals(Completo.get(0).toString())) {
            Completo.remove(0);
        }
        return semejantes();
    }
    
    public static ExpresionAlgebraica[] semejantes() {
        int p = 0, y = 1;
        float coef = 0;
        ExpresionAlgebraica[] aux;
        String valor = "";
        ArrayList signo = new ArrayList();
        ArrayList coeficiente = new ArrayList();
        ArrayList variable = new ArrayList();
        ArrayList exponente = new ArrayList();
        ArrayList prueba = new ArrayList();
        aux = Exp;
        if (aux.length == 1) {
            y = 0;
        }
        for (int i = 0; i < Exp.length; i++) {
            for (int j = y; j < aux.length; j++) {
                if (Exp[i].getExponente().equals(aux[j].getExponente()) && i != j) {
                    valor = Exp[i].getSimbolo().concat(String.valueOf(Exp[i].getCoeficiente()));
                    prueba.add(aux[j].getSimbolo().concat(String.valueOf(aux[j].getCoeficiente())));
                    aux[j].setCoeficiente(0);
                } else {
                    valor = Exp[i].getSimbolo().concat(String.valueOf(Exp[i].getCoeficiente()));
                    //prueba.add(aux[j].getSimbolo().concat(String.valueOf(aux[j].getCoeficiente())));
                }
            }
            
            for (Object prueba1 : prueba) {
                
                coef = coef + Float.valueOf(prueba1.toString());
            }
            
            coef = Float.valueOf(valor) + coef;
            Exp[i].setCoeficiente(0);
            prueba.clear();
            if (coef != 0) {
                variable.add(Exp[i].getVariable());
                if (!"0".equals(Exp[i].getExponente())) {
                    exponente.add(Exp[i].getExponente());
                } else {
                    exponente.add(0);
                }
                if (coef > 0) {
                    signo.add("+");
                    coeficiente.add(coef);
                } else {
                    signo.add("-");
                    coeficiente.add(coef * -1);
                }
            }
            coef = 0;
        }
        llenado = new ExpresionAlgebraica[signo.size()];
        for (int i = 0; i < signo.size(); i++) {
            llenado[i] = new ExpresionAlgebraica(signo.get(i).toString(), Float.valueOf(coeficiente.get(i).toString()), variable.get(i).toString(), exponente.get(i).toString());
        }
        resultados.add("\nSimplificado: ");
        for (ExpresionAlgebraica llenado1 : llenado) {
            
            resultados.add(llenado1.getSimbolo() + "" + df.format(llenado1.getCoeficiente()) + "" + llenado1.getVariable() + "^" + llenado1.getExponente());
        }
        resultados.add("");
        return llenado;
    }
}
