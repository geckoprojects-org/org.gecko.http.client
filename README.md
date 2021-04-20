# ${ProjectName}


## initial setup

Copy this to an new Issue in your Project

`initial Setup`

```

- [ ] do the [Sonar Configuration](https://sonarcloud.io/organizations/geckoprojects-org/projects)
  - [ ] press `+`_(upper right)_ - Analyze new project
  - [ ] check ${project-name} and `SetUp`
  - [ ] Select `with a GitHub Action`
  - [ ] Copy the `SONAR_TOKEN`
  - [ ] NOT!!!! follow the description .about `github/workflows/build.yml`. _this still [exists](../blob/main/.github/workflows/sonar.yml) in this template._
  - [ ] in `build.gradle` rename the    `property "sonar.projectKey", "geckoprojects-org_org.gecko.github.template"` with repository name.
  - [ ] Project-Settings -  Secrets - New Repository Secrets - add `SONAR_TOKEN`
  - [ ] `git add && git commit -s -m "rename sonar properties && git push`

- [ ] Branches
  - [ ] create a `snapshot` branche `git checkout -b snapshot && git push --set-upstream origin snapshot`
  - [ ] Secure using Setting
    - [ ] Branches - add rule
      - [ ] Branch name pattern: `[ms][an][ia][np]*` `(ugly but works, protects 'main*' and 'snap*' by charachter )`
      - [ ] Require pull request reviews before merging
        - [ ] 1 reviewer
        - [ ] Dismiss stale pull request approvals when new commits are pushed
      - [ ] Require status checks to pass before merging
        - [ ] Require branches to be up to date before merging
      - [ ] Require linear history
      - [ ] Include administrators
      - [ ] NOT!!! Allow force pushes
      - [ ] NOT!!! Allow deletion

- [ ] Jenkis
   - [ ] https://devel.data-in-motion.biz/jenkins/ 
   - [ ] wait up to 15 minutes for the auto-import

- [ ] Badges in Readme - rename  project (twice)
  - [ ] CI Build: [![CI Build](https://github.com/geckoprojects-org/org.gecko.github.template/actions/workflows/build.yml/badge.svg)](https://github.com/geckoprojects-org/org.gecko.github.template/actions/workflows/build.yml)
  - [ ] License: [![License](https://github.com/geckoprojects-org/org.gecko.github.template/actions/workflows/license.yml/badge.svg)](https://github.com/geckoprojects-org/org.gecko.github.template/actions/workflows/license.yml )
  - [ ] Sonar: [![Sonar](https://github.com/geckoprojects-org/org.gecko.github.template/actions/workflows/sonar.yml/badge.svg)](https://github.com/geckoprojects-org/org.gecko.github.template/actions/workflows/sonar.yml )
  - [ ] Bugs: [![Bugs](https://sonarcloud.io/api/project_badges/measure?project=geckoprojects-org_org.gecko.github.template&metric=bugs)](https://sonarcloud.io/dashboard?id=geckoprojects-org_org.gecko.github.template)
  - [ ] Code Smells: [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=geckoprojects-org_org.gecko.github.template&metric=code_smells)](https://sonarcloud.io/dashboard?id=geckoprojects-org_org.gecko.github.template)
  - [ ] Coverage: [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=geckoprojects-org_org.gecko.github.template&metric=coverage)](https://sonarcloud.io/dashboard?id=geckoprojects-org_org.gecko.github.template)
  - [ ] Maintainability Rating: [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=geckoprojects-org_org.gecko.github.template&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=geckoprojects-org_org.gecko.github.template)
  - [ ] Quality Gate Status: [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=geckoprojects-org_org.gecko.github.template&metric=alert_status)](https://sonarcloud.io/dashboard?id=geckoprojects-org_org.gecko.github.template)
  - [ ] Reliability Rating: [![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=geckoprojects-org_org.gecko.github.template&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=geckoprojects-org_org.gecko.github.template)
  - [ ] Security Rating: [![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=geckoprojects-org_org.gecko.github.template&metric=security_rating)](https://sonarcloud.io/dashboard?id=geckoprojects-org_org.gecko.github.template)
  - [ ] Technical Debt: [![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=geckoprojects-org_org.gecko.github.template&metric=sqale_index)](https://sonarcloud.io/dashboard?id=geckoprojects-org_org.gecko.github.template)
  - [ ] Vulnerabilities: [![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=geckoprojects-org_org.gecko.github.template&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=geckoprojects-org_org.gecko.github.template)

```
