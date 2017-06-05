package controller;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import dao.IntegranteDAO;
import dao.PessoaDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Integrante;
import model.Pessoa;

/**
 *
 * @author Marco
 */
public class ManterIntegranteController extends HttpServlet {

    private Integrante integrante;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        String acao = request.getParameter("acao");
        if (acao.equals("prepararOperacao")) {
            prepararOperacao(request, response);
        }
        if (acao.equals("confirmarOperacao")) {
            confirmarOperacao(request, response);
        }

    }

    public void prepararOperacao(HttpServletRequest request, HttpServletResponse response) throws ServletException, ClassNotFoundException, SQLException {
        try {
            String operacao = request.getParameter("operacao");
            request.setAttribute("operacao", operacao);
            request.setAttribute("pessoas", PessoaDAO.getInstance().obterPessoas());
            if (!operacao.equals("incluir")) {
                Integer idIntegrante = Integer.parseInt(request.getParameter("idIntegrante"));
                integrante = IntegranteDAO.getInstance().getIntegrante(idIntegrante);
                request.setAttribute("integrante", integrante);
            }
            RequestDispatcher view = request.getRequestDispatcher("/manterIntegrante.jsp");
            view.forward(request, response);
        } catch (ServletException e) {
            throw e;
        } catch (IOException e) {
            throw new ServletException(e);
        }

    }

    public void confirmarOperacao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String operacao = request.getParameter("operacao");
            int matricula = Integer.parseInt(request.getParameter("txtMatricula"));
            String cargaHorariaDisponivel = request.getParameter("txtCargaHorariaDisponivel");
            //chave estrangeira
            Integer idPessoa = Integer.parseInt(request.getParameter("selectPessoa"));
            Pessoa pessoa = null;
            if (idPessoa != 0) {
                pessoa = PessoaDAO.getInstance().getPessoa(idPessoa);
            }
            switch (operacao) {
                case "incluir":
                    integrante = new Integrante(matricula, cargaHorariaDisponivel, pessoa);
                    IntegranteDAO.getInstance().salvar(integrante);
                    break;
                case "editar":
                    integrante.setCargaHorariaDisponivel(cargaHorariaDisponivel);
                    integrante.setFKpessoa(pessoa);
                    IntegranteDAO.getInstance().alterar(integrante);
                    break;
                case "excluir":
                    IntegranteDAO.getInstance().excluir(integrante);
                    break;
                default:
                    break;
            }
            RequestDispatcher view = request.getRequestDispatcher("PesquisarIntegranteController");
            view.forward(request, response);

        } catch (ServletException e) {
            throw e;
        } catch (IOException e) {
            throw new ServletException(e);
        }
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

        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ManterIntegranteController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ManterIntegranteController.class.getName()).log(Level.SEVERE, null, ex);
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
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ManterIntegranteController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ManterIntegranteController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
