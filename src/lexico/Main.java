package lexico;

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
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
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

@SuppressWarnings("serial")
public class Main extends JFrame {

	private static final List<Token> NON_UNIQUE_TOKEN = new ArrayList<Token>(
			Arrays.asList(Token.ID, Token.CONST_INT, Token.CONST_REAL, Token.CONST_STR));
	private static final Long STRING_LENGHT = 2147483647L;
	private JPanel contentPane;
	private JTextArea textAreaFile;
	private JTextArea textAreaCompile;
	private JLabel lblFile;
	private List<String> tempToken;

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
		setBounds(100, 100, 1010, 780);
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

		textAreaCompile = new JTextArea();
		textAreaCompile.setEditable(false);
		JScrollPane scrollTextAreaCompile = new JScrollPane(textAreaCompile, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollTextAreaCompile.setBounds(564, 42, 420, 688);
		scrollTextAreaCompile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		contentPane.add(scrollTextAreaCompile);

		lblFile = new JLabel("");
		lblFile.setBounds(149, 11, 252, 20);
		contentPane.add(lblFile);

		JLabel lblCompile = new JLabel("Analizador L\u00E9xico:");
		lblCompile.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCompile.setBounds(564, 11, 145, 23);
		contentPane.add(lblCompile);

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
			}
		}

	}

	private void compile() {
		tempToken = new ArrayList<String>();
		Reader inputString = new StringReader(textAreaFile.getText());
		BufferedReader readerTextAreaFile = new BufferedReader(inputString);
		Lexico lexico = new Lexico(readerTextAreaFile);
		String output = "";
		Boolean error = false;
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("ts.txt", false));
			writer.append("NOMBRE;TOKEN;TIPO;VALOR;LONG\n");
			Token token;
			while ((token = lexico.yylex()) != null && !error) {
				switch (token) {
				case ESPACIO:
				case ESPCHAR:
				case COMMENT: {
					break;
				}
				case ERROR: {
					output += "Error, caracter no permitido <" + lexico.lexeme + "> en la linea " + lexico.lexemeLine
							+ "\n";
					error = true;
					break;
				}
				case ERROR_COMMENT: {
					output += "Error, comentario mal formado en la linea " + lexico.lexemeLine + "\n";
					error = true;
					break;
				}
				case CONST_INT:
				case CONST_REAL:
				case CONST_STR:
					error = exceedsLenght(token, lexico.lexeme);
					if (error) {
						output += "Error, longitud no permitida, lexema <" + lexico.lexeme + "> en la linea "
								+ lexico.lexemeLine + "\n";
						break;
					}
				default:
					output += "Token " + token + ", lexema <" + lexico.lexeme + "> en la linea " + lexico.lexemeLine
							+ "\n";
					if (NON_UNIQUE_TOKEN.contains(token)) {
						if (!existToken(lexico.lexeme)) {
							writer.append(lexico.lexeme + ";" + token + ";;" + lexico.lexeme + ";"
									+ lexico.lexeme.length() + "\n");
							tempToken.add(lexico.lexeme);
						}
					}
				}
			}
			writer.close();
			readerTextAreaFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		textAreaCompile.setText(output);
	}

	private Boolean existToken(String lexeme) {
		for (String word : tempToken) {
			if (lexeme.equals(word))
				return true;
		}
		return false;
	}

	@SuppressWarnings("incomplete-switch")
	private Boolean exceedsLenght(Token token, String value) {
		Boolean exceeds = false;
		switch (token) {
		case CONST_STR:
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
