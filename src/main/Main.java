package main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import lexico.Lexico;
import lexico.Token;
import sintactico.parser;
import sintactico.sym;

@SuppressWarnings("serial")
public class Main extends JFrame {

	private static final Long STRING_LENGHT = 2147483647L;
	private JPanel contentPane;
	private JTextArea textAreaFile;
	private JTextArea textAreaLexico;
	private JTextArea textAreaSintax;
	private JLabel lblFile;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
					frame.setResizable(false);
					frame.setTitle("Analizador Léxico");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1448, 780);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textAreaFile = new JTextArea();
		JScrollPane scrollTextAreaFile = new JScrollPane(textAreaFile, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollTextAreaFile.setBounds(10, 42, 420, 688);
		scrollTextAreaFile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		contentPane.add(scrollTextAreaFile);

		textAreaLexico = new JTextArea();
		textAreaLexico.setEditable(false);
		JScrollPane scrollTextAreaLexico = new JScrollPane(textAreaLexico, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollTextAreaLexico.setBounds(564, 42, 420, 688);
		scrollTextAreaLexico.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		contentPane.add(scrollTextAreaLexico);

		textAreaSintax = new JTextArea();
		textAreaSintax.setEditable(false);
		JScrollPane scrollTextAreaSintax = new JScrollPane(textAreaSintax, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollTextAreaSintax.setBounds(1000, 42, 420, 688);
		scrollTextAreaSintax.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		contentPane.add(scrollTextAreaSintax);

		lblFile = new JLabel("");
		lblFile.setBounds(149, 11, 252, 20);
		contentPane.add(lblFile);

		JLabel lblLexico = new JLabel("Analizador Léxico:");
		lblLexico.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblLexico.setBounds(564, 11, 145, 23);
		contentPane.add(lblLexico);

		JLabel lblSintax = new JLabel("Analizador Sintáctico:");
		lblSintax.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSintax.setBounds(1000, 11, 145, 23);
		contentPane.add(lblSintax);

		JButton btnOpen = new JButton("Abrir archivo");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openFile();
			}
		});
		btnOpen.setBounds(22, 11, 109, 23);
		contentPane.add(btnOpen);

		JButton btnCompile = new JButton("Compilar");
		btnCompile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				compile();
			}
		});
		btnCompile.setBounds(451, 370, 89, 23);
		contentPane.add(btnCompile);

	}

	private void openFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.showOpenDialog(null);
		File file = fileChooser.getSelectedFile();
		if (file != null) {
			String fileName = file.getAbsolutePath();
			lblFile.setText(fileName);
			try {
				FileReader fr = new FileReader(fileName);
				BufferedReader br = new BufferedReader(fr);
				textAreaFile.read(br, null);
				br.close();
			} catch (Exception e) {
				textAreaFile.setText("Ocurrió un error leyendo el archivo.");
				e.printStackTrace();
			}
		}

	}

	private void compile() {
		Reader inputString = new StringReader(textAreaFile.getText());
		BufferedReader readerTextAreaFile = new BufferedReader(inputString);
		Boolean lexicalError = lexicalAnalysis(readerTextAreaFile);
		if (!lexicalError)
			sintaxAnalysis(readerTextAreaFile);
		else
			textAreaSintax.setText("Ocurrió un error en el análisis léxico.");
	}

	private Boolean lexicalAnalysis(BufferedReader reader) {
		Lexico lexico = new Lexico(reader);
		String output = "";
		Boolean error = false;
		Boolean hasElements = false;
		try {

			java_cup.runtime.Symbol symbol = lexico.next_token();
			while (!(symbol.sym == sym.EOF) && !error) {
				hasElements = true;
				Token token = Token.fromValueSym(symbol.sym);
				switch (token) {
				case ERROR: {
					output += "Error, caracter no permitido <" + symbol.value + "> en la linea " + symbol.right
							+ ", columna " + symbol.left + "\n";
					error = true;
					break;
				}
				case COMMENT_ERROR: {
					output += "Error, comentario mal formado en la linea " + symbol.right + ", columna " + symbol.left
							+ "\n";
					error = true;
					break;
				}
				case CONST_INT:
				case CONST_REAL:
				case CONST_STRING:
					error = exceedsLenght(token, symbol.value.toString());
					if (error) {
						output += "Error, longitud no permitida, lexema <" + symbol.value + "> en la linea "
								+ symbol.right + ", columna " + symbol.left + "\n";
						break;
					}
				default:
					output += "Token " + token.name() + ", lexema <" + symbol.value + "> en la linea " + symbol.right
							+ ", columna " + symbol.left + "\n";
				}
				symbol = lexico.next_token();
			}
			if (!hasElements)
				error = true;
			
		} catch (Exception e) {
			textAreaLexico.setText("Ocurrió un error durante el analisis léxico.");
			e.printStackTrace();
		}

		textAreaLexico.setText(output);
		if (!hasElements)
			textAreaLexico.setText("No se encontraron elementos para analizar.");
		return error;
	}

	@SuppressWarnings("deprecation")
	private void sintaxAnalysis(BufferedReader reader) {
		Reader inputString = new StringReader(textAreaFile.getText());
		BufferedReader readerTextAreaFile = new BufferedReader(inputString);
		Lexico lexico = new Lexico(readerTextAreaFile);
		lexico.yyreset(readerTextAreaFile);
		parser parser = new parser(lexico);
		try {
			parser.parse();
			textAreaSintax.setText(parser.output);
			constructTable(parser.toAddToken);
		} catch (Exception e) {
			textAreaSintax.setText("Ocurrió un error durante el analisis sintáctico.");
			e.printStackTrace();
		}
	}

	private void constructTable(List<String> toAddTable) {	
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("ts.txt", false));
			writer.append("NOMBRE;TOKEN;TIPO;VALOR;LONG\n");
			for (String value : toAddTable) {
				String[] toAdd = value.split(";");
				String tipo = toAdd[2].equals("null") ? "" : toAdd[2];
				writer.append(toAdd[0] + ";" + toAdd[1] + ";" + tipo+";" + toAdd[0] + ";"
						+ toAdd[0].length() + "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("incomplete-switch")
	private Boolean exceedsLenght(Token token, String value) {
		Boolean exceeds = false;
		switch (token) {
		case CONST_STRING:
			if (value.length() > STRING_LENGHT)
				exceeds = true;
			break;
		case CONST_INT:
			try {
				Integer.parseInt(value);
			} catch (Exception e) {
				exceeds = true;
			}
			break;
		case CONST_REAL:
			Float constFloat = Float.parseFloat(value);
			if (constFloat.isInfinite())
				exceeds = true;
			break;
		}
		return exceeds;
	}
}
