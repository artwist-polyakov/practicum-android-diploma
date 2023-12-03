package ru.practicum.android.diploma.common.data.dto

/**
 * Конвертирует из JSON-ответа валюту и ее символ
 * Поддерживает
 * Российский рубль (RUR / RUB)
 * Белорусский рубль (BYR)
 * Доллар (USD)
 * Евро (EUR)
 * Казахстанский тенге (KZT)
 * Украинская гривна (UAH)
 * Азербайджанский манат (AZN)
 * Узбекский сум (UZS)
 * Грузинский лари (GEL)
 * Киргизский сом (KGT)
 */
enum class CurrencyIds(val code: String, val symbol: String) {
    RUR("RUR", "\u20BD"), // Символ рубля
    RUB("RUB", "\u20BD"), // Символ рубля
    BYR("BYR", "Br"),
    USD("USD", "$"),
    KZT("KZT", "₸"),
    UAH("UAH", "₴"),
    AZN("AZN", "₼"),
    UZS("UZS", "лв"),
    GEL("GEL", "₾"),
    KGS("KGS", "лв"),
    EUR("EUR", "€");
}
