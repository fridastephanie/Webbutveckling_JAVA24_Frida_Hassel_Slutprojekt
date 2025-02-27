package se.gritacademy.webfulkoping.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {

    /**
     * Avslutar den aktiva sessionen (så användarnen loggas ut) och vidarebefodrar begäran till index.sidan (/)
     *
     * @param req an {@link HttpServletRequest} object that contains the request the client has made of the servlet     *
     * @param res an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession();
            session.invalidate();
            res.sendRedirect(req.getContextPath() + "/");
        } catch(Throwable e){
            e.printStackTrace();
            req.setAttribute("message", "Ett oväntat fel har uppstått, vänligen försök igen senare!");
            req.getRequestDispatcher("view/user/index.jsp").forward(req, res);
        }
    }
}