package lexico;

public enum Token {
	
	BEGIN(2),
	END(3),
	DECLARE(4),
	ENDDECLARE(5),
	TIPO_INT(6),
	TIPO_FLOAT(7),
	TIPO_STRING(8),
	WHILE(9),
	IF(10),
	ELSE(11),
	PRINT(12),
	OP_MENOR(13),
	OP_MAYOR(14),
	OP_MENOR_IGUAL(15),
	OP_MAYOR_IGUAL(16),
	OP_DIST(17),
	OP_IGUAL(18),
	ASIGN(19),
	ASIGN_DECLR(20),
	OP_AND(21),
	OP_OR(22),
	OP_NOT(23),
	OP_SUM(24),
	OP_RES(25),
	OP_MULT(26),
	OP_DIV(27),
	P_A(28),
	P_C(29),
	C_A(30),
	C_C(31),
	L_A(32),
	L_C(33),
	MAP(34),
	ID(35),
	CONST_INT(36),
	CONST_REAL(37),
	CONST_STRING(38),
	COMA(39),
	ERROR(101),
	COMMENT_ERROR(100);
	
	private Integer value;

	Token(Integer value) {
		this.value = value;
	}
	
	public Integer getValue() {
		return value;
	}
	
	public static Token fromValueSym(Integer value) {
		for (Token token : Token.values())
			if (token.getValue() == value)
				return token;
		return ERROR;
	}
	
}
