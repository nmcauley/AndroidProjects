import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorController {
    Calculator BinaryCalculator;
    CalculatorView CalculatorInterface;

    public CalculatorController(Calculator model){
        this.BinaryCalculator = model;
        this.CalculatorInterface = new CalculatorView();
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();

        CalculatorController controller = new CalculatorController(calculator);
        controller.CalculatorInterface.initializeView();
    }

    public void clickNumberButton(int button){
        if(this.BinaryCalculator.operation.equals("")){
            //blank
            if(BinaryCalculator.firstOperand == 0) {
                BinaryCalculator.setFirstOperand(button);
            }else{
                //not blank
                String i = "" + BinaryCalculator.firstOperand + button;
                BinaryCalculator.setFirstOperand(Integer.parseInt(i));
            }

        }else {
            //blank
            if (BinaryCalculator.secondOperand == 0) {
                BinaryCalculator.setSecondOperand(button);
            } else {
                //not blank
                String j = "" + BinaryCalculator.secondOperand + button;
                BinaryCalculator.setSecondOperand(Integer.parseInt(j));
            }
        }
    }

    public void clickOperand(String operand){
        BinaryCalculator.setOperation(operand);

    }

    public void clickResult(){
        BinaryCalculator.performOperation(BinaryCalculator.firstOperand,
                BinaryCalculator.secondOperand, BinaryCalculator.operation);

        BinaryCalculator.firstOperand = BinaryCalculator.result;
        BinaryCalculator.secondOperand = 0;
    }

    public void clickClear() {
        BinaryCalculator.clear();
    }

    public class CalculatorView extends JFrame {


        public CalculatorView() {
            setTitle("CalculatorView");
            setSize(400, 400);
            initializeView();
        }

        private void initializeView() {
            JPanel panel = new JPanel();
            JLabel display = new JLabel();
//            display.setBounds(200, 200, 400 , 400);
//            display.setSize(new Dimension(350, 350));

            // Create JButton and JPanel
            JButton Button1 = new JButton("Sum");
            Button1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    clickOperand("+");
                    display.setText(BinaryCalculator.firstOperand + " " + BinaryCalculator.operation + " " + BinaryCalculator.secondOperand);
                }
            });
            JButton Button2 = new JButton("Diff");
            Button2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    clickOperand("-");
                    display.setText(BinaryCalculator.firstOperand + " " + BinaryCalculator.operation + " " + BinaryCalculator.secondOperand);
                }
            });
            JButton Button3 = new JButton("1");
            Button3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    clickNumberButton(1);
                    System.out.println(BinaryCalculator.firstOperand + ", " + BinaryCalculator.secondOperand);
                    display.setText(BinaryCalculator.firstOperand + " " + BinaryCalculator.operation + " " + BinaryCalculator.secondOperand);
                }
            });
            JButton Button4 = new JButton("0");
            Button4.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    clickNumberButton(0);
                    System.out.println(BinaryCalculator.firstOperand + ", " + BinaryCalculator.secondOperand);
                    display.setText(BinaryCalculator.firstOperand + " " + BinaryCalculator.operation + " " + BinaryCalculator.secondOperand);
                }
            });
            JButton Button5 = new JButton("=");
            Button5.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    clickResult();
                    display.setText(BinaryCalculator.result + "");
                }
            });
            JButton Button6 = new JButton("A/C");
            Button6.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    clickClear();
                    System.out.println(BinaryCalculator.firstOperand + ", " + BinaryCalculator.secondOperand);
                    display.setText("");
                }
            });
            // Add button to JPanel
            panel.add(Button1);
            panel.add(Button2);
            panel.add(Button3);
            panel.add(Button4);
            panel.add(Button5);
            panel.add(Button6);

            panel.add(display);

            // And JPanel needs to be added to the JFrame itself!
            this.getContentPane().add(panel);

            setVisible(true);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }

    }
}
