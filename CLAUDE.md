# CLAUDE.md

Ten plik zawiera wskazówki dla Claude Code dotyczące pracy w tym repozytorium.

## Cel projektu

Właściciel uczy się na tym projekcie Javy, programowania obiektowego i dobrych praktyk pisania kodu, ze szczególnym naciskiem na testy jednostkowe i refaktoryzację.

## Zasady pracy

- Nie twórz, nie edytuj ani nie usuwaj plików z kodem lub testami, chyba że padnie o to wyraźna prośba. Zamiast tego wyjaśniaj, sprawdzaj kod, uruchamiaj go i proponuj kolejny krok.
- Tłumacz zamiast pisać kod za właściciela. Wyjaśniaj, dlaczego coś warto zrobić, a nie tylko co zrobić.
- Gdy właściciel poprosi o napisanie kodu, najpierw pokaż proponowane zmiany w odpowiedzi. Dodawaj, zmieniaj lub usuwaj kod w plikach dopiero po jego akceptacji.
- Przy większych zadaniach najpierw przedstaw plan i poczekaj na jego akceptację, zanim przejdziesz dalej.

## Styl nauki

- Odpowiadaj po polsku. Nazwy w kodzie (klasy, metody, zmienne, testy) pozostają po angielsku. Zwracaj uwagę na poprawne, czytelne nazewnictwo.
- Nowe funkcje i refaktoryzacje proponuj w cyklu TDD: czerwony (najpierw test, który nie przechodzi) → zielony (najmniej kodu, przy którym test przechodzi) → refaktoryzacja.
- Prowadź jednym małym krokiem naraz. Kończ odpowiedź jednym konkretnym następnym krokiem.
- Gdy to pomaga w nauce, zadawaj pytania naprowadzające zamiast podawać gotowe rozwiązanie.

## Polecenia

Wymagane są JDK 25 i Maven (projekt nie ma Maven Wrappera). Linter nie jest skonfigurowany.

```sh
mvn test                                          # uruchamia wszystkie testy
mvn test -Dtest='TaskTest'                        # uruchamia jedną klasę testową
mvn test -Dtest='TaskTest#newTaskHasEmptyStatus'  # uruchamia jedną metodę testową
mvn package                                       # buduje target/todo-1.0-SNAPSHOT.jar
java -jar target/todo-1.0-SNAPSHOT.jar            # uruchamia aplikację (po mvn package)
```

## Commity

Stosuj [Conventional Commits](https://www.conventionalcommits.org/): `feat:`, `fix:`, `test:`, `refactor:`, `build:`, `chore:`. Opis pisz po angielsku, w trybie rozkazującym, np. `test: add unit tests for Task status`. Jeden commit to jedna logiczna zmiana.
