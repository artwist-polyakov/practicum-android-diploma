package ru.practicum.android.diploma.data.db.entity

@Entity (tableName = "favourite_vacancy_table")
data class VacancyEntity (
    @PrimaryKey
    val vacancyId :Int,

    "contacts": {
    "email": "user@example.com",
    "name": "Имя",
    "phones": [
    {
        "city": "985",
        "comment": null,
        "country": "7",
        "number": "000-00-00"
    }
    ]
},
    "created_at": "2013-07-08T16:17:21+0400",
    "department": {
    "id": "HH-1455-TECH",
    "name": "HeadHunter::Технический департамент"
},
    "description": "Работа хороша",
    "driver_license_types": [
    {
        "id": "A"
    },
    {
        "id": "B"
    }
    ],
    "employer": {
    "alternate_url": "https://hh.ru/employer/1455",
    "blacklisted": false,
    "id": "1455",
    "logo_urls": {
        "90": "https://hh.ru/employer-logo/289027.png",
        "240": "https://hh.ru/employer-logo/289169.png",
        "original": "https://hh.ru/file/2352807.png"
    },
    "name": "HeadHunter",
    "trusted": true,
    "url": "https://api.hh.ru/employers/1455"
},
    "employment": {
    "id": "full",
    "name": "Полная занятость"
},
    "experience": {
    "id": "between1And3",
    "name": "От 1 года до 3 лет"
},
    "expires_at": "2013-08-08T16:17:21+0400",
    "has_test": true,
    "hidden": false,
    "id": "8331228",
    "initial_created_at": "2013-06-08T16:17:21+0400",
    "insider_interview": {
    "id": "12345",
    "url": "https://hh.ru/interview/12345?employerId=777"
},
    "key_skills": [
    {
        "name": "Прием посетителей"
    },
    {
        "name": "Первичный документооборот"
    }
    ],
    "languages": [
    {
        "id": "eng",
        "level": {
        "id": "b2",
        "name": "B2 — Средне-продвинутый"
    },
        "name": "Английский"
    }
    ],
    "manager": {
    "id": "1"
},
    "name": "Секретарь",
    "premium": true,
    "previous_id": "123456",
    "professional_roles": [
    {
        "id": "96",
        "name": "Программист, разработчик"
    }
    ],
    "published_at": "2013-07-08T16:17:21+0400",
    "response_letter_required": true,
    "response_notifications": true,
    "response_url": null,
    "salary": {
    "currency": "RUR",
    "from": 30000,
    "gross": true,
    "to": null
},
    "schedule": {
    "id": "fullDay",
    "name": "Полный день"
},
    "test": {
    "required": false
},
    "type": {
    "id": "open",
    "name": "Открытая"
},
    "video_vacancy": {
    "video_url": "https://host/video/123"
},
    "working_days": [
    {
        "id": "only_saturday_and_sunday",
        "name": "Работа только по сб и вс"
    }
    ],
    "working_time_intervals": [
    {
        "id": "from_four_to_six_hours_in_a_day",
        "name": "Можно работать сменами по 4-6 часов в день"
    }
    ],
    "working_time_modes": [
    {
        "id": "start_after_sixteen",
        "name": "Можно начинать работать после 16-00"
    }
    ]
}
)
