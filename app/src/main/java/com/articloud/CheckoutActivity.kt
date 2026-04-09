package com.articloud

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.articloud.adapter.CheckoutSummaryAdapter
import com.articloud.databinding.ActivityCheckoutBinding
import com.articloud.model.CartManager

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private var currentStep = 1
    private var selectedPayment = "card" // card, transfer, yape

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.parseColor("#0D0D0D")
        window.decorView.systemUiVisibility = 0

        // Prellenar nombre desde sesión
        val name = SessionManager.getName(this)
        binding.etFullName.setText(name)

        setupPaymentOptions()
        setupButtons()
        updateStep(1)
    }

    private fun setupPaymentOptions() {
        binding.optionCard.setOnClickListener { selectPayment("card") }
        binding.optionTransfer.setOnClickListener { selectPayment("transfer") }
        binding.optionYape.setOnClickListener { selectPayment("yape") }
    }

    private fun selectPayment(method: String) {
        selectedPayment = method

        // Reset todos
        binding.optionCard.setBackgroundResource(R.drawable.bg_input)
        binding.optionTransfer.setBackgroundResource(R.drawable.bg_input)
        binding.optionYape.setBackgroundResource(R.drawable.bg_input)
        binding.checkCard.setBackgroundResource(R.drawable.bg_check_inactive)
        binding.checkTransfer.setBackgroundResource(R.drawable.bg_check_inactive)
        binding.checkYape.setBackgroundResource(R.drawable.bg_check_inactive)
        binding.checkCard.text = ""
        binding.checkTransfer.text = ""
        binding.checkYape.text = ""

        // Activar seleccionado
        when (method) {
            "card" -> {
                binding.optionCard.setBackgroundResource(R.drawable.bg_payment_selected)
                binding.checkCard.setBackgroundResource(R.drawable.bg_check_active)
                binding.checkCard.text = "✓"
                binding.layoutCardData.visibility = View.VISIBLE
            }
            "transfer" -> {
                binding.optionTransfer.setBackgroundResource(R.drawable.bg_payment_selected)
                binding.checkTransfer.setBackgroundResource(R.drawable.bg_check_active)
                binding.checkTransfer.text = "✓"
                binding.layoutCardData.visibility = View.GONE
            }
            "yape" -> {
                binding.optionYape.setBackgroundResource(R.drawable.bg_payment_selected)
                binding.checkYape.setBackgroundResource(R.drawable.bg_check_active)
                binding.checkYape.text = "✓"
                binding.layoutCardData.visibility = View.GONE
            }
        }
    }

    private fun setupButtons() {
        binding.btnBack.setOnClickListener {
            if (currentStep > 1) updateStep(currentStep - 1)
            else finish()
        }

        binding.btnNext.setOnClickListener {
            when (currentStep) {
                1 -> {
                    if (!validateStep1()) return@setOnClickListener
                    updateStep(2)
                }
                2 -> {
                    if (!validateStep2()) return@setOnClickListener
                    setupConfirmStep()
                    updateStep(3)
                }
                3 -> {
                    confirmOrder()
                }
            }
        }
    }

    private fun validateStep1(): Boolean {
        if (binding.etFullName.text.isNullOrBlank()) {
            binding.etFullName.error = "Requerido"
            return false
        }
        if (binding.etPhone.text.isNullOrBlank()) {
            binding.etPhone.error = "Requerido"
            return false
        }
        if (binding.etAddress.text.isNullOrBlank()) {
            binding.etAddress.error = "Requerido"
            return false
        }
        if (binding.etCity.text.isNullOrBlank()) {
            binding.etCity.error = "Requerido"
            return false
        }
        return true
    }

    private fun validateStep2(): Boolean {
        if (selectedPayment == "card") {
            if (binding.etCardNumber.text.isNullOrBlank()) {
                binding.etCardNumber.error = "Requerido"
                return false
            }
            if (binding.etCardName.text.isNullOrBlank()) {
                binding.etCardName.error = "Requerido"
                return false
            }
            if (binding.etExpiry.text.isNullOrBlank()) {
                binding.etExpiry.error = "Requerido"
                return false
            }
            if (binding.etCvv.text.isNullOrBlank()) {
                binding.etCvv.error = "Requerido"
                return false
            }
        }
        return true
    }

    private fun setupConfirmStep() {
        val items = CartManager.getItems()
        val adapter = CheckoutSummaryAdapter(items)
        binding.recyclerConfirm.layoutManager = LinearLayoutManager(this)
        binding.recyclerConfirm.adapter = adapter

        val subtotal = CartManager.getSubtotal()
        val freeShip = CartManager.hasFreeShipping()
        binding.confirmSubtotal.text = "S/ ${String.format("%,.0f", subtotal)}"
        binding.confirmShipping.text = if (freeShip) "Gratis" else "A calcular"
        binding.confirmTotal.text = "S/ ${String.format("%,.0f", subtotal)}"
    }

    private fun confirmOrder() {
        CartManager.clear()
        Toast.makeText(this, "✓ ¡Pedido confirmado! Gracias por tu compra.", Toast.LENGTH_LONG).show()
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        })
        finish()
    }

    private fun updateStep(step: Int) {
        currentStep = step

        // Mostrar/ocultar layouts
        binding.layoutStep1.visibility = if (step == 1) View.VISIBLE else View.GONE
        binding.layoutStep2.visibility = if (step == 2) View.VISIBLE else View.GONE
        binding.layoutStep3.visibility = if (step == 3) View.VISIBLE else View.GONE

        // Actualizar indicadores
        updateStepIndicator(binding.step1Circle, step >= 1)
        updateStepIndicator(binding.step2Circle, step >= 2)
        updateStepIndicator(binding.step3Circle, step >= 3)

        // Color líneas
        binding.line1.setBackgroundColor(
            if (step >= 2) Color.parseColor("#C8A96A") else Color.parseColor("#333333")
        )
        binding.line2.setBackgroundColor(
            if (step >= 3) Color.parseColor("#C8A96A") else Color.parseColor("#333333")
        )

        // Texto del botón
        binding.btnNext.text = when (step) {
            1 -> "Continuar al pago"
            2 -> "Continuar"
            3 -> "Confirmar pedido"
            else -> "Continuar"
        }
    }

    private fun updateStepIndicator(view: TextView, active: Boolean) {
        view.setBackgroundResource(
            if (active) R.drawable.bg_step_active else R.drawable.bg_step_inactive
        )
        view.setTextColor(
            if (active) Color.BLACK else Color.parseColor("#888888")
        )
    }
}