# Домашнее задание 1.
1. Создать проект со следующими пользовательскими элементами: TextView, EditText, Button, Switch, CheckBox, ToggleButton. Для работы использовать LinearLayout. Использовать различные шрифты, цвета, размеры, прочие атрибуты.
2. Создать ещё один макет (если не получается, то проект) с несколькими EditText, где использовать атрибут inputType: text, textPersonName, phone, number, textPassword, textEmailAddress и другие значения.
3. Добавить в проект элемент CalendarView.
4. Разобраться, что такое параметр ems [^1].

[^1]: Дополнительное задание

# Ответ по android:ems.

Если брать описание из Android Studio - Makes the TextView be exactly this many ems wide. Ems является единицей измерения.  
Для применения атрибута android:ems нужно, чтобы layout_width был равен "wrap_content".