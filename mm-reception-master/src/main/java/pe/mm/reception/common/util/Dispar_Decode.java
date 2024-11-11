package pe.mm.reception.common.util;

import java.util.HashMap;

public class Dispar_Decode {
    private HashMap code_hash = null;
    private char tem_cod;
    private char tem_dec;
    private char tem;
    private int indx;
    private int indx1;
    private int i = 0;
    private int j = 0;
    private int k = 0;
    private int limite;
    private String texto_codificado = "";
    private String texto_decodificado = "";
    private char[] code;
    private char[] decode;
    private char[] decode_tem_3;
    private char[] decode_tem_2;
    private char[] decode_tem_1;

    public Dispar_Decode()
    {
        this.indx = 0;
        this.indx1 = 1;
        this.limite = 3;
        this.code_hash = new HashMap();
        Llenar_Hash();
    }

    public String Codificar(String texto)
    {
        this.code = texto.toCharArray();
        this.texto_codificado = "";
        Reiniciar_Index();
        for (this.indx = 0; this.indx < this.code.length; this.indx += 1)
        {
            if (this.indx1 == this.limite)
            {
                this.indx1 = 1;
                this.limite += 1;
            }
            if (this.indx1 % 2 == 0) {
                this.tem_cod = ((char)(this.code[this.indx] + '\001'));
            } else {
                this.tem_cod = ((char)(this.code[this.indx] - '\001'));
            }
            if (this.indx % 2 == 0) {
                this.texto_codificado += this.tem_cod;
            } else {
                this.texto_codificado = (this.tem_cod + this.texto_codificado);
            }
            this.indx1 += 1;
        }
        this.code = this.texto_codificado.toCharArray();
        this.texto_codificado = "";
        for (this.indx = 0; this.indx < this.code.length; this.indx += 1)
        {
            this.tem_cod = this.code[this.indx];
            if (this.indx % 2 == 0) {
                this.texto_codificado = (this.tem_cod + this.texto_codificado);
            } else {
                this.texto_codificado += this.tem_cod;
            }
        }
        this.code = this.texto_codificado.toCharArray();
        this.texto_codificado = "";
        for (this.indx = 0; this.indx < this.code.length; this.indx += 1)
        {
            this.tem_cod = this.code[this.indx];
            if (this.indx % 2 == 0) {
                this.texto_codificado += this.tem_cod;
            } else {
                this.texto_codificado = (this.tem_cod + this.texto_codificado);
            }
        }
        return this.texto_codificado;
    }

    public String Decodificar(String texto)
    {
        this.decode_tem_3 = texto.toCharArray();
        this.decode_tem_2 = new char[this.decode_tem_3.length];
        this.decode_tem_1 = new char[this.decode_tem_3.length];
        this.decode = new char[this.decode_tem_3.length];

        this.k = (this.decode_tem_3.length - 1);
        if (this.decode_tem_3.length % 2 == 0)
        {
            this.i = 0;
            for (this.j = (this.decode_tem_3.length - 1); (this.i < this.decode_tem_3.length) || (this.j >= 0); this.j -= 1)
            {
                if ((this.i < this.decode_tem_3.length) && (this.k >= 0))
                {
                    this.decode_tem_2[this.k] = this.decode_tem_3[this.i];this.k -= 1;
                }
                if ((this.j >= 0) && (this.k >= 0))
                {
                    this.decode_tem_2[this.k] = this.decode_tem_3[this.j];this.k -= 1;
                }
                this.i += 1;
            }
        }
        this.i = 0;
        for (this.j = (this.decode_tem_3.length - 1); (this.i < this.decode_tem_3.length) || (this.j >= 0); this.j -= 1)
        {
            if ((this.j >= 0) && (this.k >= 0))
            {
                this.decode_tem_2[this.k] = this.decode_tem_3[this.j];this.k -= 1;
            }
            if ((this.i < this.decode_tem_3.length) && (this.k >= 0))
            {
                this.decode_tem_2[this.k] = this.decode_tem_3[this.i];this.k -= 1;
            }
            this.i += 1;
        }
        this.k = (this.decode_tem_2.length - 1);
        if (this.decode_tem_2.length % 2 == 0)
        {
            this.i = 0;
            for (this.j = (this.decode_tem_2.length - 1); (this.i < this.decode_tem_2.length) || (this.j >= 0); this.j -= 1)
            {
                if ((this.j >= 0) && (this.k >= 0))
                {
                    this.decode_tem_1[this.k] = this.decode_tem_2[this.j];this.k -= 1;
                }
                if ((this.i < this.decode_tem_2.length) && (this.k >= 0))
                {
                    this.decode_tem_1[this.k] = this.decode_tem_2[this.i];this.k -= 1;
                }
                this.i += 1;
            }
        }
        this.i = 0;
        for (this.j = (this.decode_tem_2.length - 1); (this.i < this.decode_tem_2.length) || (this.j >= 0); this.j -= 1)
        {
            if ((this.i < this.decode_tem_2.length) && (this.k >= 0))
            {
                this.decode_tem_1[this.k] = this.decode_tem_2[this.i];this.k -= 1;
            }
            if ((this.j >= 0) && (this.k >= 0))
            {
                this.decode_tem_1[this.k] = this.decode_tem_2[this.j];this.k -= 1;
            }
            this.i += 1;
        }
        this.k = (this.decode_tem_1.length - 1);
        if (this.decode_tem_1.length % 2 == 0)
        {
            this.i = 0;
            for (this.j = (this.decode_tem_1.length - 1); (this.i < this.decode_tem_1.length) || (this.j >= 0); this.j -= 1)
            {
                if ((this.i < this.decode_tem_1.length) && (this.k >= 0))
                {
                    this.decode[this.k] = this.decode_tem_1[this.i];this.k -= 1;
                }
                if ((this.j >= 0) && (this.k >= 0))
                {
                    this.decode[this.k] = this.decode_tem_1[this.j];this.k -= 1;
                }
                this.i += 1;
            }
        }
        this.i = 0;
        for (this.j = (this.decode_tem_1.length - 1); (this.i < this.decode_tem_1.length) || (this.j >= 0); this.j -= 1)
        {
            if ((this.j >= 0) && (this.k >= 0))
            {
                this.decode[this.k] = this.decode_tem_1[this.j];this.k -= 1;
            }
            if ((this.i < this.decode_tem_1.length) && (this.k >= 0))
            {
                this.decode[this.k] = this.decode_tem_1[this.i];this.k -= 1;
            }
            this.i += 1;
        }
        this.texto_decodificado = "";
        Reiniciar_Index();
        for (this.indx = 0; this.indx < this.decode.length; this.indx += 1)
        {
            if (this.indx1 == this.limite)
            {
                this.indx1 = 1;
                this.limite += 1;
            }
            if (this.indx1 % 2 == 0) {
                this.tem_dec = ((char)(this.decode[this.indx] - '\001'));
            } else {
                this.tem_dec = ((char)(this.decode[this.indx] + '\001'));
            }
            this.texto_decodificado += this.tem_dec;

            this.indx1 += 1;
        }
        return this.texto_decodificado;
    }

    private void Reiniciar_Index()
    {
        this.indx1 = 1;
        this.limite = 3;
    }

    private void Llenar_Hash()
    {
        this.code_hash.put("0", "(");
        this.code_hash.put("1", ")");
        this.code_hash.put("2", "[");
        this.code_hash.put("3", "]");
        this.code_hash.put("4", "{");
        this.code_hash.put("5", "}");
        this.code_hash.put("6", "<");
        this.code_hash.put("7", ">");
        this.code_hash.put("8", "+");
        this.code_hash.put("9", "-");
        this.code_hash.put(".", "*");
        this.code_hash.put("-", "/");
        this.code_hash.put("[", "=");
        this.code_hash.put("]", "@");
        this.code_hash.put("{", "#");
        this.code_hash.put("}", "$");
        this.code_hash.put("_", "%");
        this.code_hash.put(":", "&");
        this.code_hash.put(",", "-");
        this.code_hash.put("\"", "_");
    }
}
