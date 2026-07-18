package dev.gustavo.countries.detekt.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtProperty

class ViewModelDependencyRule(config: Config) : Rule(config) {
    override val issue = Issue(
        javaClass.simpleName,
        Severity.CodeSmell,
        "ViewModels should not depend on implementation classes directly. Use interfaces instead.",
        Debt.FIVE_MINS
    )

    override fun visitClass(klass: KtClass) {
        if (klass.name?.endsWith("ViewModel") == true) {
            super.visitClass(klass)
        }
    }

    override fun visitParameter(parameter: KtParameter) {
        val typeReference = parameter.typeReference?.text ?: return
        if (typeReference.endsWith("Impl")) {
            report(CodeSmell(issue, Entity.from(parameter), "ViewModel depends on implementation class: $typeReference"))
        }
    }

    override fun visitProperty(property: KtProperty) {
        val typeReference = property.typeReference?.text ?: return
        if (typeReference.endsWith("Impl")) {
            report(CodeSmell(issue, Entity.from(property), "ViewModel has a property of implementation class type: $typeReference"))
        }
    }
}
