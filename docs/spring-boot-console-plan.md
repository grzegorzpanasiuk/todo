# Plan: aplikacja todo na Spring Boot

Data: 2026-11-26

## Cel

Właściciel uczy się Spring Boot na własnej aplikacji todo. Migracja odbywa się w dwóch fazach, tak aby każda wprowadzała jedną nową rzecz:

1. **Faza 1, konsola na Spring Boot.** Aplikacja dalej działa w konsoli, ale obiekty tworzy i łączy kontener Springa. Uczymy się kontenera, beanów i wstrzykiwania zależności (DI), bez HTTP.
2. **Faza 2, aplikacja www.** Konsolę zastępuje strona z formularzem. Uczymy się kontrolerów, szablonów i testów `MockMvc`.

Faza 2 sprawdza, czy podział na warstwy z fazy 1 jest dobry: wymieniamy tylko warstwę interfejsu (konsolę na kontroler), a `TaskService` i `TaskRepository` zostają bez zmian.

### Założenia

- Logika biznesowa się nie zmienia. Trafia tylko do odpowiednich warstw i klas.
- Zadania nadal są przechowywane w pamięci. Baza danych to osobny etap po fazie 2.
- **Antycel:** kod, także testowy, którego właściciel dokładnie nie rozumie. Przy każdym kroku wyjaśniamy nowe elementy Springa i uzasadniamy, dlaczego kod trafia do danej warstwy.
- Praca w cyklu TDD (czerwony → zielony → refaktoryzacja), małymi krokami. Kod pisze właściciel.
- W fazie 1 zachowanie konsoli zostaje takie samo. Bezpieczną zmianę potwierdzamy testami i porównaniem wyjścia aplikacji z wersją sprzed zmiany (ten sam scenariusz na wejściu).
- Jeden commit to jedna logiczna zmiana ([Conventional Commits](https://www.conventionalcommits.org/)). Commit po każdym pełnym cyklu TDD.

## Gałęzie Git

```
main                      ← aplikacja konsolowa bez Springa
 └── spring-boot-console  ← faza 1 (obecny fokus)
      └── spring-boot-web ← faza 2, tworzona z końcowego stanu spring-boot-console
```

- Każda z trzech gałęzi to „główna” gałąź swojej linii. Pracujemy na krótkich gałęziach roboczych od niej, z PR-em z powrotem do niej.
- Poprawki z `spring-boot-console` można później scalać do `spring-boot-web`. Ten kierunek działa dobrze.
- Kierunek z `main` do gałęzi Springa jest trudny, bo kod `Main` zostanie rozłożony na kilka klas. Po rozgałęzieniu linie rozwijają się niezależnie, a pojedyncze poprawki przenosimy ręcznie albo przez `git cherry-pick`.
- Gałąź `main-console-cleanup` (zmiana nazwy `addTaskOrReturnErrors`, `parseTaskNumber`) należy do linii `main` i nie wchodzi do fazy 1.

## Warstwy i ich odpowiedzialność

| Klasa | Warstwa | Na jakie pytanie odpowiada |
|---|---|---|
| `Task` | domena | „Jakie reguły ma pojedyncze zadanie?” (poprawny opis, oznaczanie jako zrobione) |
| `TaskRepository` | magazyn | „Co mam zapisane i jak to znaleźć?” (dodaj, znajdź, usuń, nadaj id) |
| `TaskService` | przypadki użycia | „Co ma się stać, gdy użytkownik chce coś zrobić?” (dodaj zadanie, oznacz jako zrobione…) |
| `TaskConsole` (faza 1) / `TaskController` (faza 2) | interfejs | „Co wpisał lub wysłał użytkownik i co mu pokazać?” |

Zasady:
- interfejs rozmawia tylko z serwisem, a serwis z repozytorium i domeną,
- repozytorium nie zawiera reguł biznesowych, więc w etapie z bazą danych wymienia się tylko je.

## Faza 1: gdzie trafia kod z `main`

| Dziś | Po fazie 1 | Dlaczego |
|---|---|---|
| `Task` w całości | `Task`, bez zmian | Reguły jednego zadania nie zależą od konsoli ani od Springa. |
| `TaskList` | `TaskRepository` (`@Repository`) | Tylko przechowuje i wyszukuje. „Repository” to konwencja Springa. |
| `Main.addTask` | `TaskService` | Przypadek użycia „dodaj zadanie”: sprawdź opis, a jeśli jest poprawny, zapisz. |
| `Main.findDescriptionErrors` | `TaskService` | Część przypadku „dodaj zadanie”. Patrz „Kompromisy”. |
| logika z `case LIST` | `TaskService.findAllTasks` | Interfejs nie sięga do repozytorium bezpośrednio. |
| logika z `case DELETE` | `TaskService.deleteTask` | Przypadek użycia. Na razie cienki, ale cała komunikacja przechodzi przez serwis. |
| logika z `case MARK` | `TaskService.markTaskAsDone` | Ciąg decyzji: znajdź, sprawdź, oznacz. Typowy przypadek użycia. |
| pętla `main`, `switch`, `readTrimmedLine`, `displayMessage`, `addTaskAndReport`, `printAllTasks`, `formatTask`, `formatStatus`, stałe formatu | `TaskConsole` (`@Component`, `CommandLineRunner`) | Wczytywanie i wypisywanie to warstwa interfejsu. |
| `Main.main` | `TodoApplication.main` (`@SpringBootApplication`) | Aplikację uruchamia Spring, który tworzy obiekty i wstrzykuje zależności. |
| `Command` | `Command`, bez zmian | Nadal służy konsoli. Zniknie w fazie 2. |
| `MainTest` (`addTask`, `findDescriptionErrors`) | `TaskServiceTest` | Testy idą za metodami. |
| `MainTest` (`readTrimmedLine`) | `TaskConsoleTest` | Testy idą za metodami. |
| `TaskListTest` | `TaskRepositoryTest` | Zmiana nazwy razem z klasą. |

## Faza 1: kroki

Konsola działa przez cały czas. Każdy krok kończy się zielonymi testami, porównaniem wyjścia (gdy krok dotyka `Main`) i commitem.

### Krok 1. Gałąź ✅

`spring-boot-console` utworzona od `main` (`0d0cec2`) i wypchnięta na GitHub.

### Krok 2. Spring Boot w projekcie

- `pom.xml`:
  - rodzic `spring-boot-starter-parent` (najnowsza stabilna wersja 4.x, sprawdzimy na start.spring.io),
  - zależności `spring-boot-starter` (rdzeń, bez web) i `spring-boot-starter-test`,
  - `spring-boot-maven-plugin` zamiast konfiguracji `maven-jar-plugin`.
- Wersją JUnit od teraz zarządza Spring Boot. Sprawdzamy, czy wszystkie testy nadal przechodzą.
- Nowa klasa `TodoApplication` z `@SpringBootApplication`.
- Do kroku 5 klasą startową jara nadal jest `Main`. W projekcie są dwie metody `main`, więc wskazujemy ją jawnie.
- Pierwszy test: kontekst Springa się podnosi.
- Wyjaśnienia: rodzic Mavena i zarządzanie wersjami, startery, co robi `@SpringBootApplication`, `mvn spring-boot:run`.
- Commit: `build: add Spring Boot`.

### Krok 3. `TaskList` → `TaskRepository`

- Zmiana nazwy klasy i testów (`TaskRepositoryTest`) narzędziem IDE.
- Adnotacja `@Repository`, dzięki której klasa staje się beanem.
- **Decyzja w tym kroku:** co z prywatnym konstruktorem i fabryką `empty()`? Do wyboru: publiczny konstruktor albo metoda `@Bean` w klasie `@Configuration`, która woła `empty()`.
- Wyjaśnienia: skanowanie komponentów, zasięg singleton, `@Repository` jako odmiana `@Component`.
- Commit: `refactor: rename TaskList to TaskRepository` (+ ewentualnie osobny commit z adnotacją).

### Krok 4. `TaskService`, cztery cykle TDD

Przy każdym cyklu `Main` od razu zaczyna używać serwisu, tworzonego ręcznie: `new TaskService(repository)`. To jest DI bez Springa. Porównanie wyjścia potwierdza, że zachowanie się nie zmieniło.

| Cykl | Metoda | Skąd | Commit |
|---|---|---|---|
| 4a | dodawanie (zwraca listę błędów) + prywatne `findDescriptionErrors` | `Main.addTask`, `Main.findDescriptionErrors`, testy z `MainTest` | `refactor: move adding tasks to TaskService` |
| 4b | `findAllTasks` | `case LIST` | `refactor: list tasks via TaskService` |
| 4c | `deleteTask` (zwraca `boolean`) | `case DELETE` | `refactor: delete tasks via TaskService` |
| 4d | `markTaskAsDone` | `case MARK` | `refactor: mark tasks as done via TaskService` |

- Dokładna nazwa metody dodawania do ustalenia w cyklu 4a.
- **Decyzja w cyklu 4d:** jak zwrócić trzy wyniki (oznaczone / już było zrobione / nie istnieje).
- Wyjaśnienia: wstrzykiwanie przez konstruktor, `@Service`, dlaczego testy serwisu nie potrzebują Springa.

### Krok 5. `Main` → `TaskConsole`

- `TaskConsole` jako `@Component` z `CommandLineRunner`. Do klasy trafia pętla i cały kod konsolowy z `Main`.
- `TaskService` wstrzykiwany przez konstruktor. Teraz Spring robi to, co w kroku 4 robiliśmy ręcznie.
- `TodoApplication` staje się klasą startową jara. `Main` znika, a testy `readTrimmedLine` przechodzą do `TaskConsoleTest`.
- `application.properties`: wyłączenie banera i logów startowych, żeby nie mieszały się z wyjściem aplikacji.
- **Decyzja w tym kroku:** `@SpringBootTest` uruchamia też `CommandLineRunner`, więc test kontekstu zacząłby czytać wejście. Do wyboru: włączanie `TaskConsole` właściwością, pusty strumień wejścia w teście albo rezygnacja z testu całego kontekstu.
- Porównanie wyjścia: identyczne jak przed migracją.
- Wyjaśnienia: cykl życia aplikacji bez serwera (koniec `run` = koniec programu), `CommandLineRunner`, `application.properties`.
- Commit: `refactor: run console UI as a Spring CommandLineRunner`.

### Krok 6. Dokumentacja

- Aktualizacja poleceń w `CLAUDE.md` (`mvn spring-boot:run`, `java -jar`).
- Commit: `docs: update commands for Spring Boot`.

### Stan po fazie 1

```
com.grzegorzpanasiuk
├── TodoApplication     @SpringBootApplication
├── Task                domena
├── TaskRepository      @Repository
├── TaskService         @Service
├── TaskConsole         @Component, CommandLineRunner
└── Command             komendy konsoli
```

## Faza 2: aplikacja www (zarys)

Gałąź `spring-boot-web` tworzona z końcowego stanu `spring-boot-console`.

**Ustalenia:**
- strona generowana na serwerze w szablonach Thymeleaf, bez frameworka JavaScript,
- układ „A”: lista zadań na górze, pod nią formularz, a pod formularzem komunikat o błędzie,
- przy każdym zadaniu przycisk **Done** (tylko dla niezrobionych) i **Delete**; pusta lista pokazuje „Task list is empty.”,
- teksty po angielsku, tak jak obecne komunikaty,
- walidacja w przeglądarce atrybutami HTML (`required`, `minlength`, `maxlength`) z komunikatami przeglądarki, a także na serwerze, który zawsze decyduje,
- reguła długości opisu jest sprawdzana w obu miejscach, ale liczby 3 i 45 mają jedno źródło: stałe w `Task`. Serwer podstawia je do szablonu przy generowaniu strony (`th:minlength="${minLength}"`), więc zmiana limitu w `Task` od razu działa w przeglądarce i na serwerze. Wpisanie liczb na stałe w HTML miałoby sens przy osobnym frontendzie, który nie ma dostępu do stałych Javy.

**Kroki:**
1. Startery web i Thymeleaf.
2. `TaskController` + `tasks.html`, każdy punkt to test `MockMvc` i sprawdzenie w przeglądarce.
   **Decyzja do podjęcia przy formularzu:** przeglądarka ma też uwzględniać przycinanie spacji. Do wyboru:
   - atrybut `pattern` (np. `\s*\S.{1,43}\S\s*`, zbudowany ze stałych `Task`) zamiast `minlength`/`maxlength`, bez JavaScriptu. Wada: ogólny komunikat przeglądarki, uzupełniony atrybutem `title`,
   - JavaScript przycinający pole. Wady: nowa technologia, a po zmianie wartości przez skrypt przeglądarka nie sprawdza `minlength`.

   Serwer w każdym wariancie przycina i sprawdza sam.
   - `GET /`: lista,
   - `POST /tasks`: dodawanie, Post/Redirect/Get, komunikat „New task added.”,
   - `POST /tasks`: błędny opis, komunikaty pod formularzem,
   - `POST /tasks/{id}/done`,
   - `POST /tasks/{id}/delete`.
3. Bezpieczeństwo wątków w `TaskRepository`, bo serwer obsługuje żądania równolegle.
4. Usunięcie `TaskConsole` i `Command` (`feat!:`).
5. Przeniesienie klas do pakietu `com.grzegorzpanasiuk.task` (układ zalecany w dokumentacji Spring Boot; widoczność pakietowa w `Task` zostaje).
6. Aktualizacja `CLAUDE.md`.

## Kompromisy i tematy na później

- **Komunikaty w serwisie.** `findDescriptionErrors` zwraca gotowe angielskie teksty, więc serwis zna treść komunikatów. Bardziej wzorcowo byłoby zwracać kody błędów, a tekst dobierać w interfejsie. Przenosimy metodę bez zmian (jedna zmiana naraz), a kody błędów to osobny refaktor.
- **Bez osobnej klasy widoku w fazie 1 (decyzja).** `TaskConsole` dalej pisze bezpośrednio do `System.out`. Pomysł mentora z klasą widoku (`ConsoleView`/`PrintViewer` z `PrintStream` przekazywanym z zewnątrz) pomijamy na gałęziach Springa, bo:
  - nic z niej nie przejdzie do fazy 2, w której widokiem jest szablon Thymeleaf z `Model` ze Spring MVC,
  - wspólny interfejs dla konsoli (pętla) i www (żądanie → odpowiedź) byłby wymuszony (YAGNI),
  - po kroku 4 decyzje są w `TaskService`, testowanym zwykłym JUnitem, a w `TaskConsole` zostaje tylko wczytywanie i wypisywanie, które sprawdza porównanie wyjścia.

  Pomysł zostaje do zrealizowania na linii `main`, gdzie konsola jest na stałe.
- **Spring Shell.** To projekt Springa do interaktywnych aplikacji konsolowych. Nie używamy go, bo `CommandLineRunner` pozostawia pętlę widoczną w naszym kodzie, a celem fazy 1 jest nauka kontenera, nie kolejnej biblioteki.
- **Baza danych** po fazie 2. Wymieniamy wtedy tylko `TaskRepository`, a serwis może wymagać drobnych poprawek, np. inny typ zwracany przez `deleteById` w Spring Data JPA.
