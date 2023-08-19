package com.example.javaprohw8;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

import static com.example.javaprohw8.App.emf;

@Controller
public class Web {
    @GetMapping("/create-transactions")
    @Transactional
    public String createTransactions() {
        emf = Persistence.createEntityManagerFactory("Bank");
        EntityManager em = emf.createEntityManager();

        Operations transaction1 = new Operations("Sender1", "SenderLastName1", "Receiver1", "ReceiverLastName1", 100.0, "USD");
        Operations transaction2 = new Operations("Sender2", "SenderLastName2", "Receiver2", "ReceiverLastName2", 200.0, "EUR");

        em.getTransaction().begin();
        em.persist(transaction1);
        em.persist(transaction2);
        em.getTransaction().commit();

        em.close();
        return "redirect:/"; // Redirect to the transactions page after creating transactions
    }

    @GetMapping("/")
    public String showTransactions(Model model) {
        emf = Persistence.createEntityManagerFactory("Bank");
        EntityManager em = emf.createEntityManager();
        Query query = em.createQuery("SELECT o FROM Operations o", Operations.class);
        List<Operations> list = (List<Operations>) query.getResultList();
        em.close();

        model.addAttribute("transactions", list);
        return "index";
    }
    @GetMapping("/totalamount")
    public String showTotalAmount(Model model) {
        emf = Persistence.createEntityManagerFactory("Bank");
        EntityManager em = emf.createEntityManager();

        Double totalMoneyUAH = em.createQuery("SELECT SUM(a.balanceUAH) FROM Account a", Double.class)
                .getSingleResult();

        Double totalMoneyUSD = em.createQuery("SELECT SUM(a.balanceUSD) FROM Account a", Double.class)
                .getSingleResult();

        Double totalMoneyEUR = em.createQuery("SELECT SUM(a.balanceEUR) FROM Account a", Double.class)
                .getSingleResult();

        em.close();

        model.addAttribute("totalMoneyUAH", totalMoneyUAH);
        model.addAttribute("totalMoneyUSD", totalMoneyUSD);
        model.addAttribute("totalMoneyEUR", totalMoneyEUR);

        return "totalamount";
    }
}
