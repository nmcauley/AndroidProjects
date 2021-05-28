//
//  Converter.cpp
//  CurrencyConverter
//
//  Created by Nolan M.V. Cauley on 5/27/21.
//

#include "Converter.hpp"

class Converter {
    //initial currency
    
    //desired conversion currency
//    vector<string> v;
    std::vector<std::string> values{"USD", "GBP", "EUR"};
    
public:
    
    void displayChoices(){
        std::cout<< "Choices [" ;
        for(int i = 0; i < values.size(); i++){
            std::string temp = values.at(i);
            
            if(i != values.size() - 1) {
                temp += ", ";
            }else{
                temp += "]";
            }
            std::cout<< temp;
        }
        std::cout<<std::endl;
    };
    
};
