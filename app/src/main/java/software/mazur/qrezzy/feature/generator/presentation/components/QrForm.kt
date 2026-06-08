package software.mazur.qrezzy.feature.generator.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.core.designsystem.components.QrezzyTextInput
import software.mazur.qrezzy.feature.generator.model.QrType
import software.mazur.qrezzy.feature.generator.presentation.model.QrFieldError
import software.mazur.qrezzy.feature.generator.presentation.model.QrTypeField
import software.mazur.qrezzy.feature.generator.presentation.model.keyboardOptions
import software.mazur.qrezzy.feature.generator.presentation.model.label
import software.mazur.qrezzy.feature.generator.presentation.model.maxLength

@Composable
fun QrTypeForm(
    selectedType: QrType,
    fieldErrors: Map<QrTypeField, QrFieldError>,
    onChange: (field: QrTypeField, value: String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (selectedType) {
        is QrType.Text    -> {
            QrTextField(
                field = QrTypeField.Text,
                value = selectedType.text,
                fieldErrors = fieldErrors,
                onChange = onChange)
        }

        is QrType.Url     -> {
            QrTextField(
                field = QrTypeField.Url,
                value = selectedType.url,
                fieldErrors = fieldErrors,
                onChange = onChange)
        }

        is QrType.Phone   -> {
            QrTextField(
                field = QrTypeField.Phone,
                value = selectedType.phoneNumber,
                fieldErrors = fieldErrors,
                onChange = onChange)
        }

        is QrType.Email   -> {
            FormColumn(modifier = modifier) {
                QrTextField(
                    field = QrTypeField.EmailAddress,
                    value = selectedType.email,
                    fieldErrors = fieldErrors,
                    onChange = onChange)
                QrTextField(
                    field = QrTypeField.EmailSubject,
                    value = selectedType.subject,
                    fieldErrors = fieldErrors,
                    onChange = onChange)
                QrTextField(
                    field = QrTypeField.EmailBody,
                    value = selectedType.body,
                    fieldErrors = fieldErrors,
                    onChange = onChange)
            }
        }

        is QrType.Wifi    -> {
            FormColumn(modifier = modifier) {
                QrTextField(
                    field = QrTypeField.WifiSsid,
                    value = selectedType.ssid,
                    fieldErrors = fieldErrors,
                    onChange = onChange)
                QrTextField(
                    field = QrTypeField.WifiPassword,
                    value = selectedType.password,
                    fieldErrors = fieldErrors,
                    onChange = onChange)
            }
        }

        is QrType.Contact -> {
            FormColumn(modifier = modifier) {
                QrTextField(
                    field = QrTypeField.ContactFirstName,
                    value = selectedType.firstName,
                    fieldErrors = fieldErrors,
                    onChange = onChange)
                QrTextField(
                    field = QrTypeField.ContactLastName,
                    value = selectedType.lastName,
                    fieldErrors = fieldErrors,
                    onChange = onChange)
                QrTextField(
                    field = QrTypeField.ContactPhone,
                    value = selectedType.phone,
                    fieldErrors = fieldErrors,
                    onChange = onChange)
                QrTextField(
                    field = QrTypeField.ContactEmail,
                    value = selectedType.email,
                    fieldErrors = fieldErrors,
                    onChange = onChange)
                QrTextField(
                    field = QrTypeField.ContactCompany,
                    value = selectedType.company,
                    fieldErrors = fieldErrors,
                    onChange = onChange
                )
            }
        }
    }
}

@Composable
private fun FormColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        content()
    }
}

@Composable
private fun QrTextField(
    value: String,
    field: QrTypeField,
    fieldErrors: Map<QrTypeField, QrFieldError>,
    onChange: (field: QrTypeField, value: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val error = fieldErrors[field]

    QrezzyTextInput(
        value = value,
        placeholder = field.label,
        modifier = modifier.fillMaxWidth(),
        onValueChange = {newValue -> onChange(field, newValue)},
        singleLine = field !== QrTypeField.Text && field !== QrTypeField.EmailBody,
        maxLength = field.maxLength,
        keyboardOptions = field.keyboardOptions,
        isError = error != null,
        errorText = error?.message,
    )
}