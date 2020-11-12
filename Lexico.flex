import java_cup.runtime.Symbol;
import static lexico.Token.*;

%%


/*%cupsym Simbolo*/
%class Lexico
%type Token
%line
%ignorecase

DIGITO = [0-9]
LETRA =	[a-zA-Z]
COMA = [","]
PUNTO = ["."]
ESPACIO  = [\ ]
COM = [\"]
CHAR = ["!"|"¡"|"?"|"¿"]
CONST_STR = {COM}({LETRA}|{DIGITO}|{ESPACIO}|{CHAR}|{PUNTO}|{COMA})*{COM}
CONST_REAL = {DIGITO}+"."{DIGITO}+
CONST_INT = {DIGITO}+
ID = {LETRA}({LETRA}|{DIGITO}|_)*
ESPCHAR = [\n\t]


%{
    public String lexeme;
    public int lexemeLine;
%}


%%

<YYINITIAL> {

"begin.program" { lexeme = yytext(); lexemeLine = yyline; return BEGIN;}
"end.program" { lexeme = yytext(); lexemeLine = yyline; return END;}
"declare" { lexeme = yytext(); lexemeLine = yyline; return DECLARE;}
"enddeclare" { lexeme = yytext(); lexemeLine = yyline; return ENDDECLARE;}
"int" { lexeme = yytext(); lexemeLine = yyline; return TIPO_INT;}
"float" { lexeme = yytext(); lexemeLine = yyline; return TIPO_FLOAT;}
"string" { lexeme = yytext(); lexemeLine = yyline; return TIPO_STRING;}
"while" { lexeme = yytext(); lexemeLine = yyline; return WHILE;}
"if" { lexeme = yytext(); lexemeLine = yyline; return IF;}
"else" { lexeme = yytext(); lexemeLine = yyline; return ELSE;}
"print" { lexeme = yytext(); lexemeLine = yyline; return PRINT;}
"<" { lexeme = yytext(); lexemeLine = yyline; return OP_MENOR;}
">" { lexeme = yytext(); lexemeLine = yyline; return OP_MAYOR;}
"<=" { lexeme = yytext(); lexemeLine = yyline; return OP_MENOR_IGUAL;}
">=" { lexeme = yytext(); lexemeLine = yyline; return OP_MAYOR_IGUAL;}
"<>" { lexeme = yytext(); lexemeLine = yyline; return OP_DIST;}
"=" { lexeme = yytext(); lexemeLine = yyline; return OP_ASIGN;}
":=" { lexeme = yytext(); lexemeLine = yyline; return OP_ASIGN_DECLR;}
"and" { lexeme = yytext(); lexemeLine = yyline; return OP_AND;}
"or" { lexeme = yytext(); lexemeLine = yyline; return OP_OR;}
"not" { lexeme = yytext(); lexemeLine = yyline; return OP_NOT;}
"+"  { lexeme = yytext(); lexemeLine = yyline; return OP_SUM;}
"-" { lexeme = yytext(); lexemeLine = yyline; return OP_RES;}
"*"  { lexeme = yytext(); lexemeLine = yyline; return OP_MULT;}
"/"  { lexeme = yytext(); lexemeLine = yyline; return OP_DIV;}
"[" { lexeme = yytext(); lexemeLine = yyline; return C_A;}
"]" { lexeme = yytext(); lexemeLine = yyline; return P_C;}
"(" { lexeme = yytext(); lexemeLine = yyline; return P_A;}
")" { lexeme = yytext(); lexemeLine = yyline; return P_C;}
"{" { lexeme = yytext(); lexemeLine = yyline; return L_A;}
"}" { lexeme = yytext(); lexemeLine = yyline; return L_C;}
"map" { lexeme = yytext(); lexemeLine = yyline; return MAP;}
{ID} { lexeme = yytext(); lexemeLine = yyline; return ID;}
{CONST_INT} { lexeme = yytext(); lexemeLine = yyline; return CONST_INT;}
{CONST_STR} { lexeme = yytext(); lexemeLine = yyline; return CONST_STR;}
{CONST_REAL} { lexeme = yytext(); lexemeLine = yyline; return CONST_REAL;}
{COMA} { lexeme = yytext(); lexemeLine = yyline; return COMA;}
{ESPCHAR} { return ESPCHAR; }
{ESPACIO} { return ESPACIO; }
[<][/][^/]*[/]+([^/>][^/]*[/]+)*[>]   { return COMMENT; }
[<][/] { lexemeLine = yyline; return ERROR_COMMENT;}   

}

[^]		{ lexeme = yytext(); lexemeLine = yyline; return ERROR;}