package software.mazur.qrezzy.feature.generator.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import software.mazur.qrezzy.R
import software.mazur.qrezzy.core.designsystem.components.QrezzyTextInput
import software.mazur.qrezzy.feature.generator.mapper.keyboardOptions
import software.mazur.qrezzy.feature.generator.mapper.maxLength
import software.mazur.qrezzy.feature.generator.mapper.placeholderResId
import software.mazur.qrezzy.feature.generator.model.QrFieldError
import software.mazur.qrezzy.feature.generator.model.QrInput
import software.mazur.qrezzy.feature.generator.model.QrInputField

@Composable
fun QrTypeForm(
    qrInput: QrInput,
    fieldErrors: Map<QrInputField, QrFieldError>,
    onChange: (field: QrInputField, value: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (qrInput) {
        is QrInput.Text        -> {
            QrTextField(
                title = stringResource(R.string.generator_enter_data),
                field = QrInputField.Text,
                value = qrInput.text,
                fieldErrors = fieldErrors,
                onChange = onChange,
            )
        }

        is QrInput.Url         -> {
            QrTextField(
                title = stringResource(R.string.generator_enter_data),
                field = QrInputField.Url,
                value = qrInput.url,
                fieldErrors = fieldErrors,
                onChange = onChange,
            )
        }

        is QrInput.Phone       -> {
            QrTextField(
                title = stringResource(R.string.generator_enter_data),
                field = QrInputField.Phone,
                value = qrInput.phoneNumber,
                fieldErrors = fieldErrors,
                onChange = onChange,
            )
        }

        is QrInput.Email       -> {
            FormColumn(modifier = modifier) {
                QrTextField(
                    title = stringResource(R.string.generator_enter_data),
                    field = QrInputField.EmailAddress,
                    value = qrInput.email,
                    fieldErrors = fieldErrors,
                    onChange = onChange,
                )
                QrTextField(
                    field = QrInputField.EmailSubject,
                    value = qrInput.subject,
                    fieldErrors = fieldErrors,
                    onChange = onChange,
                )
                QrTextField(
                    field = QrInputField.EmailBody,
                    value = qrInput.body,
                    fieldErrors = fieldErrors,
                    onChange = onChange,
                )
            }
        }

        is QrInput.Wifi        -> {
            FormColumn(modifier = modifier) {
                QrTextField(
                    title = stringResource(R.string.generator_enter_data),
                    field = QrInputField.WifiSsid,
                    value = qrInput.ssid,
                    fieldErrors = fieldErrors,
                    onChange = onChange,
                )
                QrTextField(
                    field = QrInputField.WifiPassword,
                    value = qrInput.password,
                    fieldErrors = fieldErrors,
                    onChange = onChange,
                )
            }
        }

        is QrInput.Contact     -> {
            FormColumn(modifier = modifier) {
                QrTextField(
                    title = stringResource(R.string.generator_enter_data),
                    field = QrInputField.ContactFirstName,
                    value = qrInput.firstName,
                    fieldErrors = fieldErrors,
                    onChange = onChange,
                )
                QrTextField(
                    field = QrInputField.ContactLastName,
                    value = qrInput.lastName,
                    fieldErrors = fieldErrors,
                    onChange = onChange,
                )
                QrTextField(
                    field = QrInputField.ContactPhone,
                    value = qrInput.phone,
                    fieldErrors = fieldErrors,
                    onChange = onChange,
                )
                QrTextField(
                    field = QrInputField.ContactEmail,
                    value = qrInput.email,
                    fieldErrors = fieldErrors,
                    onChange = onChange,
                )
                QrTextField(
                    field = QrInputField.ContactCompany,
                    value = qrInput.company,
                    fieldErrors = fieldErrors,
                    onChange = onChange,
                )
            }
        }

        is QrInput.Sms         -> {
            FormColumn(modifier = modifier) {
                QrTextField(
                    title = stringResource(R.string.generator_enter_data),
                    field = QrInputField.SmsPhone,
                    value = qrInput.phoneNumber,
                    fieldErrors = fieldErrors,
                    onChange = onChange
                )

                QrTextField(
                    field = QrInputField.SmsMessage,
                    value = qrInput.message,
                    fieldErrors = fieldErrors,
                    onChange = onChange
                )
            }

        }

        is QrInput.GeoLocation -> {
            FormColumn(modifier = modifier) {
                QrTextField(
                    title = stringResource(R.string.generator_enter_data),
                    field = QrInputField.GeoLatitude,
                    value = qrInput.latitude,
                    fieldErrors = fieldErrors,
                    onChange = onChange
                )

                QrTextField(
                    field = QrInputField.GeoLongitude,
                    value = qrInput.longitude,
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
    field: QrInputField,
    fieldErrors: Map<QrInputField, QrFieldError>,
    onChange: (field: QrInputField, value: String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
) {
    val error = fieldErrors[field]
    QrezzyTextInput(
        value = value,
        title = title,
        placeholder = stringResource(field.placeholderResId),
        modifier = modifier.fillMaxWidth(),
        onValueChange = { newValue -> onChange(field, newValue) },
        singleLine = field !== QrInputField.Text && field !== QrInputField.EmailBody,
        maxLength = field.maxLength,
        keyboardOptions = field.keyboardOptions,
        errorText = error?.message,
    )
}
