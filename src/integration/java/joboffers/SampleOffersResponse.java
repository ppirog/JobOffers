package joboffers;

public interface SampleOffersResponse {

    default String getSampleOffersResponse2Offers() {
        return """
                [
                    {
                        "title": "AJunior Java Developer",
                        "company": "BlueSoft Sp. z o.o.",
                        "salary": "7 000 - 9 000 PLN",
                        "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-developer-bluesoft-remote-hfuanrre"
                        },
                    {
                        "title": "BJava (CMS) Developer",
                        "company": "Efigence SA",
                        "salary": "16 000 - 18 000 PLN",
                        "offerUrl": "https://nofluffjobs.com/pl/job/java-cms-developer-efigence-warszawa-b4qs8loh"
                    }                     
                ]
                """.trim();
    }

    default String getSampleOffersResponse1Offer() {
        return """
                [
                    {
                        
                        "title": "Junior Java Developer",
                        "company": "BlueSoft Sp. z o.o.",
                        "salary": "7 000 - 9 000 PLN",
                        "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-developer-bluesoft-remote-hfuanrre"
                    }
                ]
                """.trim();
    }

    default String getSampleOffersResponse0Offers() {
        return """
                []
                """.trim();
    }
}
