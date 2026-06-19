package software.mazur.qrezzy.feature.generator.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.core.designsystem.components.QrezzyTextInput
import software.mazur.qrezzy.feature.generator.mapper.keyboardOptions
import software.mazur.qrezzy.feature.generator.mapper.label
import software.mazur.qrezzy.feature.generator.mapper.maxLength
import software.mazur.qrezzy.feature.generator.model.QrInput
import software.mazur.qrezzy.feature.generator.model.QrInputField
import software.mazur.qrezzy.feature.generator.presentation.model.QrFieldError

@Composable
fun QrTypeForm(
    qrInput: QrInput,
    fieldErrors: Map<QrInputField, QrFieldError>,
    onChange: (field: QrInputField, value: String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (qrInput) {
        is QrInput.Text    -> {
            QrTextField(
                field = QrInputField.Text,
                value = qrInput.text,
                fieldErrors = fieldErrors,
                onChange = onChange)
        }

        is QrInput.Url     -> {
            QrTextField(
                field = QrInputField.Url,
                value = qrInput.url,
                fieldErrors = fieldErrors,
                onChange = onChange)
        }

        is QrInput.Phone   -> {
            QrTextField(
                field = QrInputField.Phone,
                value = qrInput.phoneNumber,
                fieldErrors = fieldErrors,
                onChange = onChange)
        }

        is QrInput.Email   -> {
            FormColumn(modifier = modifier) {
                QrTextField(
                    field = QrInputField.EmailAddress,
                    value = qrInput.email,
                    fieldErrors = fieldErrors,
                    onChange = onChange)
                QrTextField(
                    field = QrInputField.EmailSubject,
                    value = qrInput.subject,
                    fieldErrors = fieldErrors,
                    onChange = onChange)
                QrTextField(
                    field = QrInputField.EmailBody,
                    value = qrInput.body,
                    fieldErrors = fieldErrors,
                    onChange = onChange)
            }
        }

        is QrInput.Wifi    -> {
            FormColumn(modifier = modifier) {
                QrTextField(
                    field = QrInputField.WifiSsid,
                    value = qrInput.ssid,
                    fieldErrors = fieldErrors,
                    onChange = onChange)
                QrTextField(
                    field = QrInputField.WifiPassword,
                    value = qrInput.password,
                    fieldErrors = fieldErrors,
                    onChange = onChange)
            }
        }

        is QrInput.Contact -> {
            FormColumn(modifier = modifier) {
                QrTextField(
                    field = QrInputField.ContactFirstName,
                    value = qrInput.firstName,
                    fieldErrors = fieldErrors,
                    onChange = onChange)
                QrTextField(
                    field = QrInputField.ContactLastName,
                    value = qrInput.lastName,
                    fieldErrors = fieldErrors,
                    onChange = onChange)
                QrTextField(
                    field = QrInputField.ContactPhone,
                    value = qrInput.phone,
                    fieldErrors = fieldErrors,
                    onChange = onChange)
                QrTextField(
                    field = QrInputField.ContactEmail,
                    value = qrInput.email,
                    fieldErrors = fieldErrors,
                    onChange = onChange)
                QrTextField(
                    field = QrInputField.ContactCompany,
                    value = qrInput.company,
                    fieldErrors = fieldErrors,
                    onChange = onChange
                )
            }
        }
    }
}

@Composable
private fun FormColumn(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
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
    field: QrInputField,
    fieldErrors: Map<QrInputField, QrFieldError>,
    onChange: (field: QrInputField, value: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val error = fieldErrors[field]

    QrezzyTextInput(
        value = value,
        placeholder = field.label,
        modifier = modifier.fillMaxWidth(),
        onValueChange = { newValue -> onChange(field, newValue) },
        singleLine = field !== QrInputField.Text && field !== QrInputField.EmailBody,
        maxLength = field.maxLength,
        keyboardOptions = field.keyboardOptions,
        isError = error != null,
        errorText = error?.message,
    )
}