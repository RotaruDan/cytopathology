package es.eucm.cytochallenge.model;

public class PreviewConfig {

    private String imagesHost, challengeHost, challengeId;

    public String getChallengeHost() {
        return challengeHost;
    }

    public void setChallengeHost(String challengeHost) {
        this.challengeHost = challengeHost;
    }

    public String getImagesHost() {
        return imagesHost;
    }

    public void setImagesHost(String imagesHost) {
        this.imagesHost = imagesHost;
    }

    public String getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }
}
