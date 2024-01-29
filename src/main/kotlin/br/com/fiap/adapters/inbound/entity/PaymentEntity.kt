package br.com.fiap.adapters.inbound.entity

import br.com.fiap.application.core.domain.Payment
import br.com.fiap.application.core.enums.PaymentMethod
import br.com.fiap.application.core.enums.PaymentStatus
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Entity(name = "payments")
data class PaymentEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    var paymentId: String? = null,

    @Column(name="order_id",unique=true, nullable = false)
    val order: String,

    val status: String = PaymentStatus.IN_PROCESS.toString(),

    @Column(nullable = false)
    val clientId: Long,

    @CreationTimestamp
    val statusUpdatedAt: LocalDateTime? = null,

    val paymentMethod: String,

    val totalValue: BigDecimal

    ) {
    @PrePersist
    fun generate() {
        paymentId = UUID.randomUUID().toString()
    }

    fun toPayment(): Payment {
        return Payment(
            id!!,
            paymentId,
            order,
            PaymentStatus.valueOf(status),
            statusUpdatedAt = statusUpdatedAt,
            clientId = clientId,
            paymentMethod = PaymentMethod.valueOf(paymentMethod),
            totalValue = totalValue

        )
    }


    final override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PaymentEntity

        if (id != other) return false
        if (paymentId != other.paymentId) return false
        if (order != other.order) return false
        if (status != other.status) return false
        if (statusUpdatedAt != other.statusUpdatedAt) return false
        return paymentMethod == other.paymentMethod
    }

}

fun Payment.toEntity(): PaymentEntity {
    return PaymentEntity(
        id = id,
        paymentId = paymentId,
        order = order!!,
        clientId = clientId!!,
        statusUpdatedAt = statusUpdatedAt,
        status = status.toString(),
        paymentMethod = paymentMethod.toString(),
        totalValue = totalValue!!
    )
}