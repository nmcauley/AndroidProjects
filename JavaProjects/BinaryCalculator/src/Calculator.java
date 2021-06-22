public class Calculator { //Model class for the binaryCalculator app
    public String operation = "";
    public int firstOperand = 0;
    public int secondOperand = 0;
    public int result = 0;

    public Calculator(){

    }

    public void clear(){
        this.operation = "";
        this.firstOperand = 0;
        this.secondOperand = 0;
        this.result = 0;
    }

    public void setFirstOperand(int firstOperand){
        this.firstOperand = firstOperand;
    }

    public void setOperation(String operation){
        this.operation = operation;
    }

    public void setSecondOperand(int secondOperand){
        this.secondOperand = secondOperand;
    }

    public void performOperation(int firstOperand, int secondOperand, String operation){
            switch(operation){
                case "+":
                    result = Integer.parseInt(Integer.toBinaryString(Integer.parseInt(String.valueOf(firstOperand), 2) +
                            Integer.parseInt(String.valueOf(secondOperand), 2)));
                    break;

                case "-":
                    if(secondOperand > firstOperand){
                        int temp = Integer.parseInt(Integer.toBinaryString(Integer.parseInt(String.valueOf(secondOperand), 2) -
                                Integer.parseInt(String.valueOf(firstOperand), 2)));
                        result = -temp;
                    }else {
                        result = Integer.parseInt(Integer.toBinaryString(Integer.parseInt(String.valueOf(firstOperand), 2) -
                                Integer.parseInt(String.valueOf(secondOperand), 2)));
                    }
                    break;
                default: System.out.println("Unrecognized operation!"); break;
            }
        firstOperand = result;
        secondOperand = 0;
    }
}
