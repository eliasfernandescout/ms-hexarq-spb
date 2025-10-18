package com.kraftbrains.mshexarqspb;

import com.kraftbrains.mshexarqspb.adapter.input.rest.dto.CreateOrderRequest;
import com.kraftbrains.mshexarqspb.adapter.input.rest.dto.OrderItemRequest;
import com.kraftbrains.mshexarqspb.adapter.input.rest.dto.OrderResponse;
import com.kraftbrains.mshexarqspb.adapter.input.rest.mapper.OrderRestMapper;
import com.kraftbrains.mshexarqspb.application.port.input.CreateOrderUseCase;
import com.kraftbrains.mshexarqspb.domain.model.FoodOrder;
import com.kraftbrains.mshexarqspb.domain.model.OrderStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class OrderDomainTest {

    @Test
    void testRichDomainModel() {
        // Criar pedido
        FoodOrder order = new FoodOrder("cust123", "João Silva", "Rua ABC, 123");

        // Testar se pedido começa como PENDING
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertTrue(order.isPending());

        // Adicionar itens (regra de negócio)
        order.addItem(createOrderItem("Pizza", 2, new BigDecimal("35.00")));
        order.addItem(createOrderItem("Refrigerante", 1, new BigDecimal("5.00")));

        // Verificar cálculo automático do total
        assertEquals(new BigDecimal("75.00"), order.getTotalAmount());
        assertEquals(2, order.getItems().size());

        // Confirmar pedido (regra de negócio)
        order.confirm();
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
        assertTrue(order.isConfirmed());

        // Preparar pedido
        order.prepare();
        assertEquals(OrderStatus.PREPARING, order.getStatus());

        // Pronto para entrega
        order.readyForDelivery();
        assertEquals(OrderStatus.READY_FOR_DELIVERY, order.getStatus());

        // Entregar
        order.deliver();
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
        assertTrue(order.isDelivered());
    }

    @Test
    void testBusinessRuleValidation() {
        FoodOrder order = new FoodOrder("cust123", "João Silva", "Rua ABC, 123");

        // Não pode confirmar pedido vazio
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            order.confirm();
        });
        assertEquals("Cannot confirm order without items", exception.getMessage());

        // Adicionar item válido
        order.addItem(createOrderItem("Pizza", 2, new BigDecimal("35.00")));

        // Agora pode confirmar
        assertDoesNotThrow(() -> order.confirm());

        // Não pode preparar sem confirmar antes
        FoodOrder order2 = new FoodOrder("cust456", "Maria", "Rua XYZ, 456");
        order2.addItem(createOrderItem("Burger", 1, new BigDecimal("25.00")));

        Exception exception2 = assertThrows(IllegalStateException.class, () -> {
            order2.prepare();
        });
        assertTrue(exception2.getMessage().contains("must be confirmed"));
    }

    @Test
    void testCannotCancelDeliveredOrder() {
        FoodOrder order = new FoodOrder("cust123", "João Silva", "Rua ABC, 123");
        order.addItem(createOrderItem("Pizza", 2, new BigDecimal("35.00")));
        order.confirm();
        order.prepare();
        order.readyForDelivery();
        order.deliver();

        // Não pode cancelar pedido já entregue
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            order.cancel();
        });
        assertEquals("Cannot cancel a delivered order", exception.getMessage());
    }

    @Test
    void testRemoveItem() {
        FoodOrder order = new FoodOrder("cust123", "João Silva", "Rua ABC, 123");
        var item1 = createOrderItem("Pizza", 2, new BigDecimal("35.00"));
        var item2 = createOrderItem("Refrigerante", 1, new BigDecimal("5.00"));

        order.addItem(item1);
        order.addItem(item2);

        assertEquals(new BigDecimal("75.00"), order.getTotalAmount());

        // Remover item
        order.removeItem(item2.getItemId());

        assertEquals(1, order.getItems().size());
        assertEquals(new BigDecimal("70.00"), order.getTotalAmount());
    }

    private com.kraftbrains.mshexarqspb.domain.model.OrderItem createOrderItem(
            String name, int quantity, BigDecimal price) {
        return new com.kraftbrains.mshexarqspb.domain.model.OrderItem(
            "prod-" + name.toLowerCase(),
            name,
            quantity,
            price
        );
    }
}

