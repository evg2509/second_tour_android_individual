package ru.nto.guestmanagement.mobile.data

import kotlinx.coroutines.delay
import ru.nto.guestmanagement.mobile.model.PassData
import ru.nto.guestmanagement.mobile.model.PassDetails
import ru.nto.guestmanagement.mobile.model.Scan
import ru.nto.guestmanagement.mobile.model.ScanDirection
import ru.nto.guestmanagement.mobile.model.Visitor

/**
 * Демонстрационный источник данных для ручного запуска приложения.
 * Каждый третий запрос завершается ошибкой, чтобы можно было увидеть «Нет связи» и «Повторить».
 */
class DemoPassRepository(private val timeProvider: TimeProvider) : PassRepository {
    private var calls = 0

    override suspend fun load(): Result<PassData> {
        delay(700)
        calls++
        if (calls % 3 == 0) return Result.failure(RuntimeException("Не удалось загрузить пропуск"))
        val now = timeProvider.now()
        val pass = PassDetails(
            id = 17,
            qrCode = "PASS-004347",
            validFrom = now.minusHours(2),
            validTo = now.plusHours(6),
            maxEntries = 2,
            revokedAt = null,
            visitor = Visitor(42, "Иванов Иван Иванович", "ООО «НТО»"),
        )
        val scans = listOf(Scan(1, now.minusHours(1), ScanDirection.ENTRY))
        return Result.success(PassData(pass, scans))
    }
}
