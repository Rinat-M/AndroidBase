package com.rino.homework02;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

public class Calculator implements Parcelable {
    private static final String TAG = "Calculator";

    private String operand1 = "";
    private String operand2 = "";
    private double operationResult;

    private boolean isCalculated = false;

    private Operation operation = null;

    private final DecimalFormat decimalFormat = new DecimalFormat("#,###.##########");

    private final StringBuilder currentInput = new StringBuilder("0");

    public Calculator() {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator(',');
        decimalFormatSymbols.setGroupingSeparator(' ');

        decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
    }

    protected Calculator(Parcel in) {
        operand1 = in.readString();
        operand2 = in.readString();
        operationResult = in.readDouble();
        isCalculated = in.readByte() != 0;
        operation = Operation.valueOf(in.readString());

        resetCurrentInputValueTo(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(operand1);
        dest.writeString(operand2);
        dest.writeDouble(operationResult);
        dest.writeByte((byte) (isCalculated ? 1 : 0));
        dest.writeString(operation.name());
        dest.writeString(currentInput.toString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Calculator> CREATOR = new Creator<Calculator>() {
        @Override
        public Calculator createFromParcel(Parcel in) {
            return new Calculator(in);
        }

        @Override
        public Calculator[] newArray(int size) {
            return new Calculator[size];
        }
    };

    public String getCurrentInput() {
        return currentInput.toString();
    }

    private void resetCurrentInputValueTo(String newValue) {
        currentInput.setLength(0);
        currentInput.append(newValue);
    }

    public String enteredValue(String value) {
        if (currentInput.length() == 0 && value.equals(",")) {
            currentInput.append("0");
        } else if ((currentInput.length() == 1 && currentInput.indexOf("0") != -1)
                && !value.equals(",")) {
            currentInput.setLength(0);
        }

        if (!(currentInput.indexOf(",") != -1 && value.equals(","))) {
            currentInput.append(value);
        }

        return currentInput.toString();
    }

    public String removeLastInputValue() {
        if (currentInput.length() == 0) {
            currentInput.append("0");
        } else if (!(currentInput.length() == 1 && currentInput.indexOf("0") != -1)) {
            currentInput.delete(currentInput.length() - 1, currentInput.length());

            if (currentInput.length() == 0) {
                currentInput.append("0");
            }
        }

        return currentInput.toString();
    }

    public void reset() {
        operand1 = "";
        operand2 = "";
        operationResult = 0;
        isCalculated = false;
        operation = null;

        resetCurrentInputValueTo("0");
    }

    public void applyOperation(Operation selectedOperation) {
        if (operation == null || isCalculated) {
            if (selectedOperation != Operation.CALCULATE) {
                operation = selectedOperation;
            } else {
                return;
            }

            isCalculated = false;
            operand2 = "";

            operand1 = currentInput.toString();

            resetCurrentInputValueTo("0");

            if (selectedOperation != Operation.PERCENT) {
                return;
            }
        } else {
            operand2 = currentInput.toString();
        }

        try {
            invokeOperation();
        } catch (Exception e) {
            Log.e(TAG, "Error during invokeOperation()", e);
        }
    }

    public void invokeOperation() throws ParseException {
        Number number1 = null;
        Number number2 = null;

        if (!operand1.isEmpty()) {
            number1 = decimalFormat.parse(operand1);
        }

        if (!operand2.isEmpty()) {
            number2 = decimalFormat.parse(operand2);
        }

        switch (operation) {
            case PERCENT:
                operationResult = number1.doubleValue() / 100;
                break;
            case DIVIDE:
                operationResult = number1.doubleValue() / number2.doubleValue();
                break;
            case MULTIPLY:
                operationResult = number1.doubleValue() * number2.doubleValue();
                break;
            case MINUS:
                operationResult = number1.doubleValue() - number2.doubleValue();
                break;
            case PLUS:
                operationResult = number1.doubleValue() + number2.doubleValue();
                break;
            case CALCULATE:
                invokeOperation();
                break;
            default:
                throw new IllegalArgumentException("Unknown operation " + operation + " found!");
        }

        resetCurrentInputValueTo(decimalFormat.format(operationResult));
        isCalculated = true;
    }

    public String getInfoAboutCurrentOperation() {
        String resultString = "";

        if (isCalculated) {
            resultString += operand1 + " " + operation + " " + operand2 + " = " + decimalFormat.format(operationResult);
        } else if (operation != null) {
            resultString += operand1 + " " + operation + " ";
        }

        return resultString;
    }

}
