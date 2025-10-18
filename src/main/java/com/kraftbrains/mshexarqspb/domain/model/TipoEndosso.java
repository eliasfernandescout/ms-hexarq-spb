package com.kraftbrains.mshexarqspb.domain.model;

/**
 * Tipos de endosso conforme legislação cambial
 */
public enum TipoEndosso {
    EM_BRANCO,      // Endosso sem especificar beneficiário
    EM_PRETO,       // Endosso com beneficiário especificado
    MANDATO,        // Endosso para cobrança (procuração)
    CAUCAO          // Endosso em garantia
}

