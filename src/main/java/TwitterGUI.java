import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class TwitterGUI<ConfigurationBuilder> {

    private JFrame frame;
    private JTextField textField;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TwitterGUI window = new TwitterGUI();
                    window.frame.setVisible(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public TwitterGUI() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        textField = new JTextField();
        textField.setBounds(248, 10, 178, 19);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        JLabel lblNewLabel = new JLabel("type your search keyword:");
        lblNewLabel.setBounds(48, 13, 236, 19);
        frame.getContentPane().add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("What is your sentiment?");
        lblNewLabel_1.setBounds(48, 60, 157, 13);
        frame.getContentPane().add(lblNewLabel_1);

        JButton btnSearchButton = new JButton("Search");
        btnSearchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TwitterAnalyzer.displayTweets(textField.getText());
            }
        });
        btnSearchButton.setBounds(291, 27, 85, 21);
        frame.getContentPane().add(btnSearchButton);

        JButton btnMostUsedWord = new JButton("MOST USED WORD");
        btnMostUsedWord.setFont(new Font("Tahoma", Font.BOLD, 8));
        btnMostUsedWord.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TwitterAnalyzer.mostMentionedWord(TwitterAnalyzer.getStatusesBaseOnTheKeyWord(textField.getText()));
            }
        });
        btnMostUsedWord.setBounds(0, 134, 142, 107);
        frame.getContentPane().add(btnMostUsedWord);

        JButton btnMostUsedEmoji = new JButton("MOST USED EMOJI");
        btnMostUsedEmoji.setFont(new Font("Tahoma", Font.BOLD, 8));
        btnMostUsedEmoji.setSize(new Dimension(150, 150));
        btnMostUsedEmoji.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TwitterAnalyzer.mostUsedEmojis(TwitterAnalyzer.getStatusesBaseOnTheKeyWord(textField.getText()));
            }
        });
        btnMostUsedEmoji.setBounds(142, 134, 142, 107);
        frame.getContentPane().add(btnMostUsedEmoji);

        JButton btnMostActiveUser = new JButton("MOST ACTIVE USER");
        btnMostActiveUser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TwitterAnalyzer.mostActiveUser(TwitterAnalyzer.getStatusesBaseOnTheKeyWord(textField.getText()));
            }
        });
        btnMostActiveUser.setFont(new Font("Tahoma", Font.BOLD, 8));
        btnMostActiveUser.setBounds(284, 134, 142, 107);
        frame.getContentPane().add(btnMostActiveUser);
        SentimentValue[] sentimentValues = {SentimentValue.VERY_NEGATIVE, SentimentValue.NEGATIVE, SentimentValue.NEUTRAL, SentimentValue.POSITIVE, SentimentValue.VERY_POSITIVE};
        JComboBox<?> comboBox = new JComboBox<Object>(sentimentValues);
        comboBox.setBounds(248, 56, 178, 21);
        frame.getContentPane().add(comboBox);

        JButton btnSearchSentiment = new JButton("Search");
        btnSearchSentiment.setActionCommand("Search");
        btnSearchSentiment.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TwitterAnalyzer.filterTweetsBasedOnSentiment((SentimentValue) comboBox.getSelectedItem(),TwitterAnalyzer.getStatusesBaseOnTheKeyWord(textField.getText()));

            }
        });
        btnSearchSentiment.setBounds(291, 75, 85, 21);
        frame.getContentPane().add(btnSearchSentiment);
    }
}
