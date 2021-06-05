//
//  main.cpp
//  PracticePrograms
//
//  Created by Nolan M.V. Cauley on 6/5/21.
//

#include <iostream>

void fib(int n);
bool palindrome(int n);

int main(int argc, const char * argv[]) {
    
    int input;
    std::cout<<"Select a program to test\n1. Fibonacci\n2. Palindrome Number" << std::endl;
    std::cin >> input;
    void *ptr;
    switch (input) {
        case 1:

//            fib();
            break;

        case 2:
            std::cout<<"You've selected Palindrome; input an integer to display the answer." << std::endl;
            std::cin>>input;
            std::cout<<"palindrome result: " << (palindrome(input)? "true" : "false")
            << std::endl;
        default:
            break;
    }

    return 0;
}

bool palindrome(const int n){
    int reversedNumber = 0, remainder, temp = n;
    
    while (temp != 0) {
        remainder = temp % 10;
        reversedNumber = reversedNumber * 10 + remainder;
        temp /= 10;
    }
    return n == reversedNumber;
}
