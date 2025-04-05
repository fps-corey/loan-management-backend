package com.example.loanmanagement.exception;

public class InvalidLoanTransitionException extends RuntimeException {
    public InvalidLoanTransitionException(String from, String to) {
        super("Invalid transition from " + from + " to " + to);
    }
}