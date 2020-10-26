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
COMMENT = "</"({LETRA}|{DIGITO}|{ESPACIO}|{CHAR}|{PUNTO}|{COMA})*"/>"
CHAR = ["!"|"¡"|"?"|"¿"]
CONST_STR = {COM}({LETRA}|{DIGITO}|{ESPACIO}|{CHAR}|{PUNTO}|{COMA})*{COM}
CONST_REAL = {DIGITO}+"."{DIGITO}+
CONST_INT = {DIGITO}+
ID = {LETRA}({LETRA}|{DIGITO}|_)*
OP_COMPARACION = ["<"|"="|">"|"<="|">="|"><"]
OP_SUM = ["+"]
OP_RES = ["-"]
OP_MULT = ["*"]
OP_DIV = ["/"]
OP_LOG = ["and"|"or"|"not"]
C_A = ["["]
C_C = ["]"]
P_A = ["("]
P_C = [")"]
L_A = ["{"]
L_C = ["}"]
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
"integer" { lexeme = yytext(); lexemeLine = yyline; return TIPO;}
"float" { lexeme = yytext(); lexemeLine = yyline; return TIPO;}
"if" { lexeme = yytext(); lexemeLine = yyline; return IF;}
"else" { lexeme = yytext(); lexemeLine = yyline; return ELSE;}
"print" { lexeme = yytext(); lexemeLine = yyline; return PRINT;}
"<" { lexeme = yytext(); lexemeLine = yyline; return OP_COMPARACION;}
">" { lexeme = yytext(); lexemeLine = yyline; return OP_COMPARACION;}
"<=" { lexeme = yytext(); lexemeLine = yyline; return OP_COMPARACION;}
">=" { lexeme = yytext(); lexemeLine = yyline; return OP_COMPARACION;}
"<>" { lexeme = yytext(); lexemeLine = yyline; return OP_COMPARACION;}
"=" { lexeme = yytext(); lexemeLine = yyline; return OP_ASIGN;}
":=" { lexeme = yytext(); lexemeLine = yyline; return OP_ASIGN;}
"and" { lexeme = yytext(); lexemeLine = yyline; return OP_LOG;}
"or" { lexeme = yytext(); lexemeLine = yyline; return OP_LOG;}
"not" { lexeme = yytext(); lexemeLine = yyline; return OP_LOG;}
{OP_SUM}  { lexeme = yytext(); lexemeLine = yyline; return OP_SUM;}
{OP_RES}  { lexeme = yytext(); lexemeLine = yyline; return OP_RES;}
{OP_MULT}  { lexeme = yytext(); lexemeLine = yyline; return OP_MULT;}
{OP_DIV}  { lexeme = yytext(); lexemeLine = yyline; return OP_DIV;}
{C_A} { lexeme = yytext(); lexemeLine = yyline; return C_A;}
{C_C} { lexeme = yytext(); lexemeLine = yyline; return P_C;}
{P_A} { lexeme = yytext(); lexemeLine = yyline; return P_A;}
{P_C} { lexeme = yytext(); lexemeLine = yyline; return P_C;}
{L_A} { lexeme = yytext(); lexemeLine = yyline; return L_A;}
{L_C} { lexeme = yytext(); lexemeLine = yyline; return L_C;}
{OP_LOG} { lexeme = yytext(); lexemeLine = yyline; return OP_LOG;}
{ID} { lexeme = yytext(); lexemeLine = yyline; return ID;}
{CONST_INT} { lexeme = yytext(); lexemeLine = yyline; return CONST_INT;}
{CONST_STR} { lexeme = yytext(); lexemeLine = yyline; return CONST_STR;}
{CONST_REAL} { lexeme = yytext(); lexemeLine = yyline; return CONST_REAL;}
{COMA} { lexeme = yytext(); lexemeLine = yyline; return COMA;}
{ESPCHAR} { return ESPCHAR; }
{ESPACIO} { return ESPACIO; }

}

[^]		{ lexeme = yytext(); lexemeLine = yyline; return ERROR;}