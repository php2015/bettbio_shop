/**
 * 
 */
package com.salesmanager.web.shop.controller;

/**
 * Interface contain constant for Controller.These constant will be used throughout
 * sm-shop to  providing constant values to various Controllers being used in the
 * application.
 * @author Umesh A
 *
 */
public interface ControllerConstants
{

    final static String REDIRECT="redirect:";
    
    interface Tiles{
        interface ShoppingCart{
            final static String shoppingCart="maincart";
        }
        
        interface Category{
            final static String category="category";
        }
        
        interface Product{
            final static String product="product";
            final static String service="service";
            final static String instrument="instrument";
            final static String consumptive="consumptive";
        }
        
        interface Market{
            final static String marketlist="marketlist";
        }
        
        interface Customer{
            final static String customer="customer";
            final static String customerLogon="customerLogon";
            final static String review="review";
            final static String register="register";
            final static String memberpoint="memberpoint";
            final static String giftOrder="giftOrder";
            final static String memberPointExchange="memberPointExchange";
            final static String changePassword="customerPassword";
            final static String customerOrders="customerOrders";
            final static String customerOrder="customerOrder";
            final static String Billing="customerAddress";
            final static String EditAddress="editCustomerAddress";
            final static String BasicInfo="editCustomerBasicInfo";
            final static String Invoice="editCustomerInvoice";
            final static String safetyCenter = "safetyCenter";
            final static String changeEmail="customerEmail";
            final static String changePhone="customerPhone";
        }
        
        interface Content{
            final static String content="content";
            final static String contactus="contactus";
            final static String information="information";
        }
        
        interface NewsContent{
            final static String newslist="newslist";
            final static String newsview = "newsview";
        }
        
        interface Pages{
            final static String notFound="404";
            final static String timeout="timeout";
        }
        
        interface Merchant{
            final static String contactUs="contactus";
        }
        
        interface Checkout{
            final static String checkout="checkout";
            final static String confirmation="confirmation";
        }
        
        interface Search{
            final static String search="search";
        }
        
        interface Software{
        	final static String softwarelist = "softwarelist";
        }
        
    }

    interface Views
    {
        interface Controllers
        {
            interface Registration
            {
                String RegistrationPage = "shop/customer/registration.html";
            }
        }
    }
}
