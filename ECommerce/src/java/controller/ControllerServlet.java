/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import cart.ShoppingCart;
import entity.Category;
import entity.Product;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import session.CategoryFacade;
import session.ProductFacade;

/**
 *
 * @author eybcegc
 */
@WebServlet(name = "ControllerServlet", loadOnStartup = 1, urlPatterns = {"/category", "/addToCart", "/viewCart", "/updateCart", "/checkout", "/purchase", "/chooseLanguage"})
public class ControllerServlet extends HttpServlet {

    @EJB
    private CategoryFacade categoryFacade;

    private String surcharge;
    
    @EJB
    private ProductFacade productFacade;
    
    @Override
    public void init(ServletConfig servletConfig) throws ServletException 
    {
        super.init(servletConfig);
        
        surcharge = servletConfig.getServletContext().getInitParameter("deliverySurcharge");
        
        getServletContext().setAttribute("categories", categoryFacade.findAll());
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userPath = request.getServletPath();

        HttpSession session = request.getSession();
        
        
        // if category page is requested
        if (userPath.equals("/category")) {
            // TODO: Implement category request

            String categoryId = request.getQueryString();
            if(categoryId != null){
                Category selectedCategory = categoryFacade.find(Short.parseShort(categoryId));
                session.setAttribute("selectedCategory", selectedCategory);
                
                Collection<Product> categoryProducts = selectedCategory.getProductCollection();
                session.setAttribute("categoryProducts", categoryProducts);
            }
            
            // if cart page is requested
        } else if (userPath.equals("/viewCart")) {
            // TODO: Implement cart page request

            String clear = request.getParameter("clear");
            if((clear != null) && clear.equals("true"))
            {
                ShoppingCart cart = (ShoppingCart)session.getAttribute("cart");
                cart.clear();
            }
            
            userPath = "/cart";

            // if checkout page is requested
        } else if (userPath.equals("/checkout")) {
            // TODO: Implement checkout page request

            ShoppingCart cart = (ShoppingCart)session.getAttribute("cart");
            cart.calculateTotal(surcharge);
            
            // if user switches language
        } else if (userPath.equals("/chooseLanguage")) {
            // TODO: Implement language request

        }

        // use RequestDispatcher to forward request internally
        String url = "/WEB-INF/view" + userPath + ".jsp";

        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String userPath = request.getServletPath();

        HttpSession session = request.getSession();
        ShoppingCart cart = (ShoppingCart)session.getAttribute("cart");
        
        // if addToCart action is called
        if (userPath.equals("/addToCart")) {
            // TODO: Implement add product to cart action
            if(cart == null)
            {
                cart = new ShoppingCart();
                session.setAttribute("cart", cart);
            }
            
            String productId = request.getParameter("productId");
            if(!productId.isEmpty())
            {
                Product product = productFacade.find(Integer.parseInt(productId));
                cart.addItem(product);
            }
            
            userPath = "/category";
            
            // if updateCart action is called
        } else if (userPath.equals("/updateCart")) {
            // TODO: Implement update cart action

            String productId = request.getParameter("productId");
            String quantity = request.getParameter("quantity");
            Product product = productFacade.find(Integer.parseInt(productId));
            cart.update(product, quantity);
            
            userPath = "/cart";
            
            // if purchase action is called
        } else if (userPath.equals("/purchase")) {
            // TODO: Implement purchase action

            userPath = "/confirmation";
        }

        // use RequestDispatcher to forward request internally
        String url = "/WEB-INF/view" + userPath + ".jsp";

        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
