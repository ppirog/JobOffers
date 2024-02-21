# jako klient aplikacji chcę widzieć oferty pracy dla Junior Java Developera
korzystamy ze zdalnego serwera HTTP (skrypt który pobiera oferty ze stron WWW)
klient musi używać tokena, żeby zobaczyć oferty
klient może się zarejestrować
aktualizacja ofert w bazie danych jest co 3 godziny 
(wtedy odpytujemy zdalny serwer z pkt. 1)
oferty w bazie nie mogą się powtarzać (decyduje url oferty)
klient może pobrać jedną ofertę pracy poprzez unikalne Id
klient może pobrać wszystkie dostępne oferty kiedy jest zautoryzowany
jeśli klient w ciągu 60 minut robi więcej niż jedno zapytanie, to dane powinny pobierać się z cache (ponieważ pobieranie z bazy danych kosztuję pieniądze naszego klienta)
klient może ręcznie dodać ofertę pracy
każda oferta pracy ma (link do oferty, nazwę stanowiska, nazwę firmy, 
zarobki (mogą być widełki))
