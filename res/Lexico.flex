package lexico;
import java_cup.runtime.*;
import sintactico.sym;

%%


%cup
%public
%class Lexico
%ignorecase
%line
%column

DIGITO = [0-9]
LETRA =	[a-zA-Z]
COMA = [","]
PUNTO = ["."]
ESPACIO  = [\ ]
COM = [\"]
ESP_CHAR = ["!"|"¡"|"?"|"¿"]
CONST_STR = {COM}({LETRA}|{DIGITO}|{ESPACIO}|{ESP_CHAR}|{PUNTO}|{COMA})*{COM}
CONST_REAL = {DIGITO}+"."{DIGITO}+
CONST_INT = {DIGITO}+
ID = {LETRA}({LETRA}|{DIGITO}|_)*


%%

<YYINITIAL> {

"begin.program" { return new Symbol(sym.BEGIN,yycolumn,yyline,yytext()); }
"end.program" { return new Symbol(sym.END,yycolumn,yyline,yytext()); }
"declare" { return new Symbol(sym.DECLARE,yycolumn,yyline,yytext()); }
"enddeclare" { return new Symbol(sym.ENDDECLARE,yycolumn,yyline,yytext()); }
"int" { return new Symbol(sym.TIPO_INT,yycolumn,yyline,yytext()); }
"float" { return new Symbol(sym.TIPO_FLOAT,yycolumn,yyline,yytext()); }
"string" { return new Symbol(sym.TIPO_STRING,yycolumn,yyline,yytext()); }
"while" { return new Symbol(sym.WHILE,yycolumn,yyline,yytext()); }
"if" { return new Symbol(sym.IF,yycolumn,yyline,yytext()); }
"else" { return new Symbol(sym.ELSE,yycolumn,yyline,yytext()); }
"print" { return new Symbol(sym.PRINT,yycolumn,yyline,yytext()); }
"<" { return new Symbol(sym.OP_MENOR,yycolumn,yyline,yytext()); }
">" { return new Symbol(sym.OP_MAYOR,yycolumn,yyline,yytext()); }
"<=" { return new Symbol(sym.OP_MENOR_IGUAL,yycolumn,yyline,yytext()); }
">=" { return new Symbol(sym.OP_MAYOR_IGUAL,yycolumn,yyline,yytext()); }
"<>" { return new Symbol(sym.OP_DIST,yycolumn,yyline,yytext()); }
"==" { return new Symbol(sym.OP_IGUAL,yycolumn,yyline,yytext()); }
"=" { return new Symbol(sym.ASIGN,yycolumn,yyline,yytext()); }
":=" { return new Symbol(sym.ASIGN_DECLR,yytext()); }
"and" { return new Symbol(sym.OP_AND,yycolumn,yyline,yytext()); }
"or" { return new Symbol(sym.OP_OR,yycolumn,yyline,yytext()); }
"not" { return new Symbol(sym.OP_NOT,yycolumn,yyline,yytext()); }
"+"  { return new Symbol(sym.OP_SUM,yycolumn,yyline,yytext()); }
"-" { return new Symbol(sym.OP_RES,yycolumn,yyline,yytext()); }
"*"  { return new Symbol(sym.OP_MULT,yycolumn,yyline,yytext()); }
"/"  { return new Symbol(sym.OP_DIV,yycolumn,yyline,yytext()); }
"[" { return new Symbol(sym.C_A,yycolumn,yyline,yytext()); }
"]" { return new Symbol(sym.C_C,yycolumn,yyline,yytext()); }
"(" { return new Symbol(sym.P_A,yycolumn,yyline,yytext()); }
")" { return new Symbol(sym.P_C,yycolumn,yyline,yytext()); }
"{" { return new Symbol(sym.L_A,yycolumn,yyline,yytext()); }
"}" { return new Symbol(sym.L_C,yycolumn,yyline,yytext()); }
"map" { return new Symbol(sym.MAP,yycolumn,yyline,yytext()); }
{ID} { return new Symbol(sym.ID,yycolumn,yyline,yytext()); }
{CONST_INT} { return new Symbol(sym.CONST_INT,yycolumn,yyline,yytext()); }
{CONST_STR} { return new Symbol(sym.CONST_STRING,yycolumn,yyline,yytext()); }
{CONST_REAL} { return new Symbol(sym.CONST_REAL,yycolumn,yyline,yytext()); }
{COMA} { return new Symbol(sym.COMA,yycolumn,yyline,yytext()); }
{ESPACIO} { }
"\n" { }
"\t" { }
[<][/][^/]*[/]+([^/>][^/]*[/]+)*[>]   { }
[<][/] { return new Symbol(sym.COMMENT_ERROR,yycolumn,yyline,yytext()); }

}

[^]	{ return new Symbol(sym.ERROR,yycolumn,yyline,yytext()); }
<<EOF>> {return new Symbol(sym.EOF);}