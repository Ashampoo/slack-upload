# slack-upload

Small Java tool using Slack SDK API to push releases to a slack channel.

Run `gradle shadowJar` to create a binary in `build/libs`.

Sample usage in GitHub Workflow:
```
  upload_to_slack:
    name: Upload to Slack
    needs: create_release
    runs-on: ubuntu-latest
    timeout-minutes: 15
    steps:
      - name: Checkout workspace (for local stored action)
        uses: actions/checkout@v3
        timeout-minutes: 5
      - name: Download artifact
        uses: actions/download-artifact@v2
        with:
          name: windows
          path: .
      - name: Set up Java JDK 17
        uses: actions/setup-java@v3
        with:
          distribution: 'temurin'
          java-version: '17.0.5+8'
      - name: Upload to Slack
        env:
          SLACK_TOKEN: ${{ secrets.SLACK_TOKEN }}
          SLACK_CHANNEL: "#photos_releases"
          PRODUCT_NAME: "Ashampoo Photos"
          RELEASE_VERSION: ${{ env.RELEASE_VERSION }}
          FILE_PATH: "ashampoo-photos.zip"
        run: java -jar tools/slack-upload-1.0-all.jar
 ```
