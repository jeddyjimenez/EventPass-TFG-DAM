package com.tfg.eventos.controlador;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.tfg.eventos.entidad.Entrada;
import com.tfg.eventos.entidad.enums.EstadoPago;
import com.tfg.eventos.servicio.EntradaService;
import com.tfg.eventos.servicio.UsuarioService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class PagoController {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;
    @Value("${stripe.success.url}")
    private String successUrl;
    @Value("${stripe.cancel.url}")
    private String cancelUrl;

    private final UsuarioService usuarioService;
    private final EntradaService entradaService;
    
    public PagoController(EntradaService entradaService, UsuarioService usuarioService){
        this.entradaService = entradaService;
        this.usuarioService = usuarioService;
    }
    @PostMapping("/pago/checkout/{entradaId}")
    public void sesionCheckout(@PathVariable Long entradaId, 
        HttpServletResponse response, 
        Authentication authentication) throws Exception{
        
        Stripe.apiKey = stripeSecretKey;
        Optional<Entrada> entradaOpt = entradaService.obtenerPorId(entradaId);
        if (entradaOpt.isEmpty()){
            response.sendRedirect("/");
            return;
        } else {
            Entrada entradaReal = entradaOpt.get();
            if (entradaReal.getAsistente().getUsuario().getEmail().equals(authentication.getName())){
                SessionCreateParams params = SessionCreateParams.builder()
        .setMode(SessionCreateParams.Mode.PAYMENT)
        .setSuccessUrl(successUrl + "?entradaId=" + entradaId)
        .setCancelUrl(cancelUrl + "?entradaId=" + entradaId)
        .addLineItem(
                SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("eur")
                                        .setUnitAmount(entradaReal.getAsistente().getEvento().getPrecio()
                                                .multiply(java.math.BigDecimal.valueOf(100)).longValue())
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName(entradaReal.getAsistente().getEvento().getNombre())
                                                        .build()
                                        )
                                        .build()
                        )
                        .build()
        )
        .build();
        Session session = Session.create(params);
        response.sendRedirect(session.getUrl());
            } else {
                response.sendRedirect("/");
                return;
            }
        }
    }
    @GetMapping("/pago/exito")
    public String pagoExito(@RequestParam Long entradaId){
        Optional<Entrada> entradaOpt = entradaService.obtenerPorId(entradaId);
        if (entradaOpt.isEmpty()) {
            return "redirect:/mis_entradas";
        }
        Entrada entradaReal = entradaOpt.get();
        entradaReal.setEstadoPago(EstadoPago.PAGADO);
        entradaService.guardar(entradaReal);
        return "redirect:/mis_entradas";
    }

    @GetMapping("/pago/cancelado")
    public String pagoCancelado() {
        return "redirect:/mis_entradas";
    }

}
