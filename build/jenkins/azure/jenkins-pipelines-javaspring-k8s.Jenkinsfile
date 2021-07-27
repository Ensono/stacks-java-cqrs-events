// Note: Variables prefixed with `dynamic_` are set in the "Checkout Dependencies" Phase.
// These use the scripted pipelines feature and `script {}` tags should only be used sparingly.

// TODO: Ensure the Pipeline works with Multiple Agents
// TODO: Adding a production environment causes the build to crash with Java Method too long
// See: https://amido-dev.visualstudio.com/Amido-Stacks/_workitems/edit/2497/

pipeline {
  agent none

  options {
    preserveStashes()
  }

  environment {
    company="amido"
    project="stacks"
    domain="java-jenkins"
    component="api"
    role="backend"
    source_branch_ref="${CHANGE_BRANCH ?: BRANCH_NAME}"
    target_branch_ref="${CHANGE_TARGET ?: " "}" // Set to a space as Jenkins doesn't support blank vars
    pull_request_number="${CHANGE_ID ?: " "}" // Set to a space as Jenkins doesn't support blank vars
    self_repo_src="java"
    self_repo_tf_src="deploy/azure/app/kube"
    self_repo_k8s_src="deploy/k8s"
    self_generic_name="stacks-java-jenkins"
    self_pipeline_repo="stacks-pipeline-templates"
    self_post_deploy_test_src="api-tests"
    self_functional_testproject_dir="${self_post_deploy_test_src}"
    yamllint_config="yamllint.conf"
    // Maven
    maven_cache_directory="./.m2"
    maven_surefire_repots_dir="./target/surefire-reports"
    maven_allowed_test_tags="Unit | Component | Integration | Functional | Performance | Smoke"
    maven_allowed_post_deploy_test_tags="@Functional or @Smoke or @Performance"
    maven_ignored_post_deploy_test_tags="@Ignore"
    maven_post_deploy_html_report_directory="target/site/serenity"
    maven_post_deploy_failsafe_reports_directory="target/failsafe-reports"
    // TF STATE CONFIG
    terraform_state_rg="amido-stacks-rg-uks"
    terraform_state_storage="amidostackstfstategbl"
    terraform_state_container="tfstate"
    // Stacks operates Terraform states based on workspaces **IT IS VERY IMPORTANT** that you ensure a unique name for each application definition
    // Furthermore **IT IS VERY IMPORTANT** that you change the name of a workspace for each deployment stage
    // there are some best practices around this if you are going for feature based environments
    // - we suggest you create a runtime variable that is dynamically set based on a branch currently running
    // **`terraform_state_workspace: `**
    // avoid running anything past dev that is not on master
    // sample value: company-webapp
    terraform_state_key="stacks-api-java-jenkins"
    // Versioning
    version_major="0"
    version_minor="0"
    version_revision="${BUILD_NUMBER}"
    // Docker Config
    container_registry_suffix=".azurecr.io"
    docker_workdir="java/"
    docker_build_additional_args="."
    docker_image_name="${self_generic_name}"
    docker_image_tag_prefix="${version_major}.${version_minor}.${version_revision}-"
    // dynamic_docker_branch_tag="" // Assigned dynamically in "Checkout Dependencies"
    // dynamic_docker_image_tag="" // Assigned dynamically in "Checkout Dependencies"
    docker_container_registry_name_nonprod="amidostacksnonprodeuncore"
    k8s_docker_registry_nonprod="${docker_container_registry_name_nonprod}${container_registry_suffix}"
    docker_container_registry_name_prod="amidostacksprodeuncore"
    k8s_docker_registry_prod="${docker_container_registry_name_prod}${container_registry_suffix}"
    docker_tag_latest_nonprod="false"
    docker_tag_latest_promotion="false"
    build_artifact_deploy_name="${self_generic_name}"
    // Vulnerability Scan
    vulnerability_scan_report_path="target"
    vulnerability_scan_report_filename="dependency-check-report.html"
    vulnerability_scan_fail_build_on_detection="false"
    // SonarScanner variables
    static_code_analysis="true"
    sonar_host_url="https://sonarcloud.io"
    sonar_project_name="stacks-java-jenkins"
    sonar_project_key="stacks-java-jenkins"
    // SONAR_TOKEN - Please define this as a Jenkins credential.
    sonar_organisation="amido"
    sonar_command="sonar-scanner"
    sonar_remote_repo="amido/stacks-java"
    sonar_pullrequest_provider="github"
    // AKS/AZURE
    // NONPROD_AZURE_CLIENT_ID - Please define this as a Jenkins credential.
    // NONPROD_AZURE_CLIENT_SECRET - Please define this as a Jenkins credential.
    // NONPROD_AZURE_TENANT_ID - Please define this as a Jenkins credential.
    // NONPROD_AZURE_SUBSCRIPTION_ID - Please define this as a Jenkins credential.
    // PROD_AZURE_CLIENT_ID - Please define this as a Jenkins credential.
    // PROD_AZURE_CLIENT_SECRET - Please define this as a Jenkins credential.
    // PROD_AZURE_TENANT_ID - Please define this as a Jenkins credential.
    // PROD_AZURE_SUBSCRIPTION_ID - Please define this as a Jenkins credential.
    // Infra
    base_domain_nonprod="nonprod.amidostacks.com"
    base_domain_internal_nonprod="nonprod.amidostacks.internal"
    base_domain_prod="prod.amidostacks.com"
    base_domain_internal_prod="prod.amidostacks.internal"
    // Functional Tests
    functional_test="true"
    // functional_test_path: "${self_functional_testproject_dir }}"
    // functional_test_artefact_path: "${self_repo_dir }}/${self_post_deploy_test_src }}"
    // functional_test_artefact_name: "post-deploy-test-artefact"
    // functional_test_artefact_download_location: "$(Pipeline.Workspace)/${functional_test_artefact_name }}"
    // Build Task Naming
    java_project_type="Java App"
    functional_test_project_type="Functional API Tests"
  }

  stages {
    stage('CI') {
      stages {
        stage('Checkout Dependencies') {
          agent {
            docker {
              // add additional args if you need to here
              // e.g.:
              // args '-v /var/run/docker.sock:/var/run/docker.sock -u 1000:999'
              // Please check with your admin on any required steps you need to take to ensure a SUDOers access inside the containers
              image "azul/zulu-openjdk-debian:11"
            }
          }

          steps {

            script {
              // Sets a variable for the pipeline script directory
              env.dynamic_build_scripts_directory = "${WORKSPACE}/${self_pipeline_repo}/scripts"

              // Sets a branch based name to be used in the Docker image tag
              env.dynamic_docker_branch_tag = sh(
                script: """#!/bin/bash
                  DOCKER_BRANCH_TAG="${env.CHANGE_ID ? "pr-${source_branch_ref}" : source_branch_ref}"
                  DOCKER_BRANCH_TAG="\${DOCKER_BRANCH_TAG//"/"/"--"}"
                  echo -n "\${DOCKER_BRANCH_TAG}"
                """,
                returnStdout: true,
                label: "Setting Docker branch tag as Jenkins Var"
              )

              // Sets the full docker tag be used as the Docker Tag
              env.dynamic_docker_image_tag = sh(
                script: """#!/bin/bash
                  BRANCH_TAG="${docker_image_tag_prefix}${dynamic_docker_branch_tag}"
                  IMAGE_TAG="\$(tr '[:upper:]' '[:lower:]' <<< "\${BRANCH_TAG}")"
                  echo -n "\${IMAGE_TAG}"
                """,
                returnStdout: true,
                label: "Setting Docker Image Tag as Jenkins Var"
              )
            }

            dir("${self_pipeline_repo}") {
              checkout([
                $class: 'GitSCM',
                branches: [[name: 'refs/tags/v1.4.12']],
                userRemoteConfigs: [[url: "https://github.com/amido/${self_pipeline_repo}"]]
              ])
            }
          }
        } // End of Checkout Dependencies stage

        stage('Yaml Lint') {
          agent {
            docker {
              // add additional args if you need to here
              image "amidostacks/ci-k8s:0.0.11"
            }
          }

          steps {
            sh(
              script: """#!/bin/bash
                bash "${dynamic_build_scripts_directory}/test-validate-yaml.bash" \
                  -a "${yamllint_config}" \
                  -b "."
              """,
              label: "Validate: Yamllint Validation"
            )
          }

        } // End of Yaml Lint stage

        stage('Terraform Validation') {
          agent {
            docker {
              // add additional args if you need to here
              image "amidostacks/ci-tf:0.0.4"
            }
          }

          steps {
            dir("${self_repo_tf_src}") {
              sh(
                script: """#!/bin/bash
                  bash "${dynamic_build_scripts_directory}/test-terraform-fmt-check.bash"
                """,
                label: "Terraform: Format Check"
              )

              sh(
                script: """#!/bin/bash
                  bash "${dynamic_build_scripts_directory}/test-terraform-validate.bash"
                """,
                label: "Terraform: Validate Check"
              )
            }
          }
        } // End of Terraform Validation Stage

        stage('Build') {
          stages {

            stage("Java Build (Java App)") {
              agent {
                docker {
                  // add additional args if you need to here
                  image "azul/zulu-openjdk-debian:11"
                }
              }

              steps {
                dir("${self_repo_src}") {

                  sh(
                    script: """#!/bin/bash
                      bash "${dynamic_build_scripts_directory}/build-maven-install.bash" \
                        -Z "${maven_cache_directory}"
                    """,
                    label: "Maven: Install Packages (${java_project_type})"
                  )

                  sh(
                    script: """#!/bin/bash
                      bash "${dynamic_build_scripts_directory}/test-maven-checkstyle-check.bash" \
                        -Z "${maven_cache_directory}"
                    """,
                    label: "Maven: Checkstyle Check (${java_project_type})"
                  )

                  sh(
                    script: """#!/bin/bash
                      bash "${dynamic_build_scripts_directory}/test-maven-format-check.bash" \
                        -Z "${maven_cache_directory}"
                    """,
                    label: "Maven: Format Check (${java_project_type})"
                  )

                  sh(
                    script: """#!/bin/bash
                      bash "${dynamic_build_scripts_directory}/test-maven-spotbugs-check.bash" \
                        -Z "${maven_cache_directory}"
                    """,
                    label: "Maven: Spotbugs Check (${java_project_type})"
                  )

                  sh(
                    script: """#!/bin/bash
                      export LC_ALL="C.UTF-8"
                      bash "${dynamic_build_scripts_directory}/test-maven-owasp-dependency-check.bash" \
                        -Y "${vulnerability_scan_fail_build_on_detection}" \
                        -Z "${maven_cache_directory}"
                    """,
                    label: "Maven: OWASP Vulnerability Scan"
                  )

                  sh(
                    script: """#!/bin/bash
                      bash "${dynamic_build_scripts_directory}/build-maven-compile.bash" \
                        -Z "${maven_cache_directory}"
                    """,
                    label: "Maven: Compile Application (${java_project_type})"
                  )

                  sh(
                    script: """#!/bin/bash
                      rm -rf "${maven_surefire_repots_dir}"

                      bash "${dynamic_build_scripts_directory}/test-maven-download-test-deps.bash" \
                        -X "${maven_allowed_test_tags}" \
                        -Y "${maven_surefire_repots_dir}" \
                        -Z "${maven_cache_directory}"
                    """,
                    label: "Test: Download Test Deps (${java_project_type})"
                  )

                  sh(
                    script: """#!/bin/bash
                      bash "${dynamic_build_scripts_directory}/test-maven-tagged-test-run.bash" \
                        -a "Unit" \
                        -Z "${maven_cache_directory}"
                    """,
                    label: "Test: Unit tests (${java_project_type})"
                  )

                  sh(
                    script: """#!/bin/bash
                      bash "${dynamic_build_scripts_directory}/test-maven-tagged-test-run.bash" \
                        -a "Component" \
                        -Z "${maven_cache_directory}"
                    """,
                    label: "Test: Component tests (${java_project_type})"
                  )

                  sh(
                    script: """#!/bin/bash
                      bash "${dynamic_build_scripts_directory}/test-maven-tagged-test-run.bash" \
                        -a "Integration" \
                        -Z "${maven_cache_directory}"
                    """,
                    label: "Test: Integration tests (${java_project_type})"
                  )

                  sh(
                    script:"""#!/bin/bash
                      bash "${dynamic_build_scripts_directory}/test-maven-generate-jacoco-report.bash" \
                        -Z "${maven_cache_directory}"
                    """,
                    label: "Generate Jacoco coverage reports (${java_project_type})"
                  )
                }

              }

              post {
                always {
                  dir("${self_repo_src}") {
                    // Publish Test Results
                    junit 'target/**/*.xml'

                    // See:
                    // https://www.jenkins.io/doc/pipeline/steps/jacoco/
                    // For Code Coverage gates for Jenkins JaCoCo.
                    jacoco(
                      execPattern: 'target/*.exec',
                      classPattern: 'target/classes',
                      sourcePattern: 'src/main/java',
                      exclusionPattern: 'src/test*'
                    )

                    // Publish OWASP Report
                    publishHTML (
                      target: [
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: "${vulnerability_scan_report_path}",
                        reportFiles: "${vulnerability_scan_report_filename}",
                        reportName: "OWASP Report (${java_project_type})",
                        reportTitles: "OWASP Report (${java_project_type})"
                      ]
                    )
                  }
                }
              } // post end

            } // End of Java Build Stage

            stage("Java Build (Functional Test)") {
              when {
                expression { "${functional_test}" == "true" }
              }

              agent {
                docker {
                  // add additional args if you need to here
                  image "azul/zulu-openjdk-debian:11"
                }
              }

              steps {
                dir("${self_functional_testproject_dir}") {

                  sh(
                    script: """#!/bin/bash
                      bash "${dynamic_build_scripts_directory}/build-maven-install.bash" \
                        -Z "${maven_cache_directory}"
                    """,
                    label: "Maven: Install Packages (${functional_test_project_type})"
                  )

                  sh(
                    script: """#!/bin/bash
                      bash "${dynamic_build_scripts_directory}/test-maven-checkstyle-check.bash" \
                        -Z "${maven_cache_directory}"
                    """,
                    label: "Maven: Checkstyle Check (${functional_test_project_type})"
                  )

                  sh(
                    script: """#!/bin/bash
                      bash "${dynamic_build_scripts_directory}/test-maven-format-check.bash" \
                        -Z "${maven_cache_directory}"
                    """,
                    label: "Maven: Format Check (${functional_test_project_type})"
                  )

                  sh(
                    script: """#!/bin/bash
                      bash "${dynamic_build_scripts_directory}/test-maven-spotbugs-check.bash" \
                        -Z "${maven_cache_directory}"
                    """,
                    label: "Maven: Spotbugs Check (${functional_test_project_type})"
                  )

                  sh(
                    script: """#!/bin/bash
                      export LC_ALL="C.UTF-8"
                      bash "${dynamic_build_scripts_directory}/test-maven-owasp-dependency-check.bash" \
                        -Y "${vulnerability_scan_fail_build_on_detection}" \
                        -Z "${maven_cache_directory}"
                    """,
                    label: "Maven: OWASP Vulnerability Scan (${functional_test_project_type})"
                  )

                  sh(
                    script: """#!/bin/bash
                      bash "${dynamic_build_scripts_directory}/build-maven-compile.bash" \
                        -Z "${maven_cache_directory}"
                    """,
                    label: "Maven: Compile Application (${functional_test_project_type})"
                  )

                  sh(
                    script: """#!/bin/bash
                      rm -rf "${maven_post_deploy_html_report_directory}"

                      bash "${dynamic_build_scripts_directory}/test-maven-post-deploy-untagged-test-check.bash" \
                        -a "${maven_allowed_post_deploy_test_tags}" \
                        -W "${maven_post_deploy_html_report_directory}" \
                        -X "${maven_post_deploy_failsafe_reports_directory}" \
                        -Y "${maven_ignored_post_deploy_test_tags}" \
                        -Z "${maven_cache_directory}"
                    """,
                    label: "Maven: Invalid Test Tag Check (${functional_test_project_type})"
                  )
                }

              }

              post {
                always {
                  dir("${self_functional_testproject_dir}") {
                    // Publish OWASP Report for API Tests
                    publishHTML (
                      target: [
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: "${vulnerability_scan_report_path}",
                        reportFiles: "${vulnerability_scan_report_filename}",
                        reportName: "OWASP Report (${functional_test_project_type})",
                        reportTitles: "OWASP Report (${functional_test_project_type})"
                      ]
                    )

                    // TODO: This puts strain on the master and may want to be replaced with other methods...
                    // This or something similar would be needed on a multi-agent build run.
                    // stash(
                    //   name: "functional-tests-artefact",
                    //   allowEmpty: false,
                    //   excludes: "src/**",
                    //   includes: "**,src/test/resources/"
                    // )
                  }
                }
              } // post end

            } // End of Java Build (API) Stage

            stage('SonarScanner') {
              when {
                expression {
                  "${static_code_analysis}" == "true"
                }
              }

              agent {
                docker {
                  // add additional args if you need to here
                  image "amidostacks/ci-sonarscanner:0.0.1"
                }
              }

              steps {
                dir("${self_repo_src}") {
                  withCredentials([
                    string(credentialsId: 'SONAR_TOKEN', variable: 'SONAR_TOKEN')
                  ]) {
                    sh(
                      script: """#!/bin/bash
                        set -exo pipefail

                        # Workaround for Jenkins not allowing a blank variable.
                        if [ "${target_branch_ref}" == ' ' ]; then
                          BASH_TARGET_BRANCH_REF=""
                        else
                          BASH_TARGET_BRANCH_REF="${target_branch_ref}"
                        fi

                        # Workaround for Jenkins not allowing a blank variable.
                        if [ "${pull_request_number}" == ' ' ]; then
                          BASH_PULL_REQUEST_NUMBER=""
                        else
                          BASH_PULL_REQUEST_NUMBER="${pull_request_number}"
                        fi

                        bash "${dynamic_build_scripts_directory}/test-sonar-scanner.bash" \
                          -a "${sonar_host_url}" \
                          -b "${sonar_project_name}" \
                          -c "${sonar_project_key}" \
                          -d "${SONAR_TOKEN}" \
                          -e "${sonar_organisation}" \
                          -f "${BUILD_NUMBER}" \
                          -g "${source_branch_ref}" \
                          -V "${sonar_command}" \
                          -W "${sonar_remote_repo}" \
                          -X "${sonar_pullrequest_provider}" \
                          -Y "\$BASH_TARGET_BRANCH_REF" \
                          -Z "\$BASH_PULL_REQUEST_NUMBER"
                      """,
                      label: "Static Analysis: SonarScanner Run"
                    )
                  }
                }

              }
            } // End of SonarScanner Stage

            stage('Docker') {
              agent {
                docker {
                  // add additional args if you need to here
                  image "amidostacks/ci-k8s:0.0.11"
                }
              }

              steps {
                dir("${docker_workdir}") {

                  sh(
                    script: """#!/bin/bash
                      bash "${dynamic_build_scripts_directory}/build-docker-image.bash" \
                        -a "${docker_build_additional_args}" \
                        -b "${docker_image_name}" \
                        -c "${dynamic_docker_image_tag}" \
                        -d "${docker_container_registry_name_nonprod}" \
                        -Z "${container_registry_suffix}"
                    """,
                    label: "Build Container Image"
                  )

                  withCredentials([
                    string(credentialsId: 'NONPROD_AZURE_CLIENT_ID', variable: 'NONPROD_AZURE_CLIENT_ID'),
                    string(credentialsId: 'NONPROD_AZURE_CLIENT_SECRET', variable: 'NONPROD_AZURE_CLIENT_SECRET'),
                    string(credentialsId: 'NONPROD_AZURE_TENANT_ID', variable: 'NONPROD_AZURE_TENANT_ID'),
                    string(credentialsId: 'NONPROD_AZURE_SUBSCRIPTION_ID', variable: 'NONPROD_AZURE_SUBSCRIPTION_ID')
                  ]) {
                    sh(
                      script: """#!/bin/bash
                        bash "${dynamic_build_scripts_directory}/util-azure-login.bash" \
                          -a "${NONPROD_AZURE_CLIENT_ID}" \
                          -b "${NONPROD_AZURE_CLIENT_SECRET}" \
                          -c "${NONPROD_AZURE_TENANT_ID}" \
                          -d "${NONPROD_AZURE_SUBSCRIPTION_ID}"
                      """,
                      label: "Login: Azure CLI"
                    )
                  }

                  sh(
                    script: """#!/bin/bash
                      bash "${dynamic_build_scripts_directory}/util-docker-image-push.bash" \
                        -a "${docker_image_name}" \
                        -b "${dynamic_docker_image_tag}" \
                        -c "${docker_container_registry_name_nonprod}" \
                        -Y "${docker_tag_latest_nonprod}" \
                        -Z "${container_registry_suffix}"
                    """,
                    label: "Push Container Image to Azure Container Registry"
                  )

                }

              }

            } // End of Docker Stage

          } // End of Build stages

        } // End of Build Stage

      } // End of CI stages

    } // End of CI Stage

    stage("Dev") {
      environment {
        environment_name="dev"
        dns_name="${environment_name}-java-api-jenkins"
        core_resource_group="amido-stacks-nonprod-eun-core"
        k8s_app_path="/api/menu"
        dns_pointer="${dns_name}.${base_domain_nonprod}"
        functional_test_base_url="https://${dns_pointer}${k8s_app_path}"
      }

      stages {

        stage("AppInfraDev") {
          agent {
            docker {
              image "amidostacks/ci-tf:0.0.4"
            }
          }

          environment {
            terraform_state_workspace="${environment_name}"
            TF_VAR_name_company="${company}"
            TF_VAR_name_project="${project}"
            TF_VAR_name_domain="${domain}"
            TF_VAR_name_component="${component}"
            TF_VAR_name_role="${role}"
            TF_VAR_name_environment="${environment_name}"
            TF_VAR_attributes="[]"
            TF_VAR_tags="{}"
            TF_VAR_resource_group_location="northeurope"
            TF_VAR_app_gateway_frontend_ip_name="amido-stacks-nonprod-eun-core"
            TF_VAR_dns_record="${dns_name}"
            TF_VAR_dns_zone_name="${base_domain_nonprod}"
            TF_VAR_dns_zone_resource_group="${core_resource_group}"
            TF_VAR_core_resource_group="${core_resource_group}"
            TF_VAR_internal_dns_zone_name="${base_domain_internal_nonprod}"
            TF_VAR_create_cosmosdb="true"
            TF_VAR_create_cache="false"
            TF_VAR_create_dns_record="true"
            TF_VAR_create_cdn_endpoint="false"
            TF_VAR_cosmosdb_sql_container="Menu"
            TF_VAR_cosmosdb_sql_container_partition_key="/id"
            TF_VAR_cosmosdb_kind="GlobalDocumentDB"
            TF_VAR_cosmosdb_offer_type="Standard"
            TF_VAR_app_insights_name="amido-stacks-nonprod-eun-core"
          }

          steps {
            dir("${self_repo_tf_src}") {

              withCredentials([
                string(credentialsId: 'NONPROD_AZURE_CLIENT_ID', variable: 'NONPROD_AZURE_CLIENT_ID'),
                string(credentialsId: 'NONPROD_AZURE_CLIENT_SECRET', variable: 'NONPROD_AZURE_CLIENT_SECRET'),
                string(credentialsId: 'NONPROD_AZURE_TENANT_ID', variable: 'NONPROD_AZURE_TENANT_ID'),
                string(credentialsId: 'NONPROD_AZURE_SUBSCRIPTION_ID', variable: 'NONPROD_AZURE_SUBSCRIPTION_ID')
              ]) {
                sh(
                  script: """#!/bin/bash
                    bash "${dynamic_build_scripts_directory}/util-azure-login.bash" \
                      -a "${NONPROD_AZURE_CLIENT_ID}" \
                      -b "${NONPROD_AZURE_CLIENT_SECRET}" \
                      -c "${NONPROD_AZURE_TENANT_ID}" \
                      -d "${NONPROD_AZURE_SUBSCRIPTION_ID}"
                  """,
                  label: "Login: Azure CLI"
                )
              }

              sh(
                script: """#!/bin/bash
                  bash "${dynamic_build_scripts_directory}/util-azure-register-pod-identity.bash"
                """,
                label: "Register Pod Identity Previews"
              )

              withCredentials([
                string(credentialsId: 'NONPROD_AZURE_CLIENT_ID', variable: 'NONPROD_TERRAFORM_BACKEND_AZURE_CLIENT_ID'),
                string(credentialsId: 'NONPROD_AZURE_CLIENT_SECRET', variable: 'NONPROD_TERRAFORM_BACKEND_AZURE_CLIENT_SECRET'),
                string(credentialsId: 'NONPROD_AZURE_TENANT_ID', variable: 'NONPROD_TERRAFORM_BACKEND_AZURE_TENANT_ID'),
                string(credentialsId: 'NONPROD_AZURE_SUBSCRIPTION_ID', variable: 'NONPROD_TERRAFORM_BACKEND_AZURE_SUBSCRIPTION_ID')
              ]) {
                sh(
                  script: """#!/bin/bash
                    bash "${dynamic_build_scripts_directory}/deploy-azure-terraform-init.bash" \
                      -a "${NONPROD_TERRAFORM_BACKEND_AZURE_CLIENT_ID}" \
                      -b "${NONPROD_TERRAFORM_BACKEND_AZURE_CLIENT_SECRET}" \
                      -c "${NONPROD_TERRAFORM_BACKEND_AZURE_TENANT_ID}" \
                      -d "${NONPROD_TERRAFORM_BACKEND_AZURE_SUBSCRIPTION_ID}" \
                      -e "${terraform_state_rg}" \
                      -f "${terraform_state_storage}" \
                      -g "${terraform_state_container}" \
                      -h "${terraform_state_key}" \
                      -i "${terraform_state_workspace}"
                  """,
                  label: "Terraform: Initialise and Set Workspace"
                )
              }

              withCredentials([
                string(credentialsId: 'NONPROD_AZURE_CLIENT_ID', variable: 'NONPROD_AZURE_CLIENT_ID'),
                string(credentialsId: 'NONPROD_AZURE_CLIENT_SECRET', variable: 'NONPROD_AZURE_CLIENT_SECRET'),
                string(credentialsId: 'NONPROD_AZURE_TENANT_ID', variable: 'NONPROD_AZURE_TENANT_ID'),
                string(credentialsId: 'NONPROD_AZURE_SUBSCRIPTION_ID', variable: 'NONPROD_AZURE_SUBSCRIPTION_ID')
              ]) {
                sh(
                  script: """#!/bin/bash
                    bash "${dynamic_build_scripts_directory}/deploy-azure-terraform-plan.bash" \
                      -a "${NONPROD_AZURE_CLIENT_ID}" \
                      -b "${NONPROD_AZURE_CLIENT_SECRET}" \
                      -c "${NONPROD_AZURE_TENANT_ID}" \
                      -d "${NONPROD_AZURE_SUBSCRIPTION_ID}"
                  """,
                  label: "Terraform: Plan"
                )

                sh(
                  script: """#!/bin/bash
                    bash "${dynamic_build_scripts_directory}/deploy-azure-terraform-apply.bash" \
                      -a "${NONPROD_AZURE_CLIENT_ID}" \
                      -b "${NONPROD_AZURE_CLIENT_SECRET}" \
                      -c "${NONPROD_AZURE_TENANT_ID}" \
                      -d "${NONPROD_AZURE_SUBSCRIPTION_ID}"
                  """,
                  label: "Terraform: Apply"
                )
              }

              // Extract needed Terraform Outputs into Jenkins Variables
              // These are used in the next stage
              script {
                env.dynamic_cosmosdb_endpoint = sh(
                  script:"""#!/bin/bash
                    terraform output cosmosdb_endpoint
                  """,
                  returnStdout: true,
                  label: "Terraform: Extract ComsosDB Endpoint URL"
                )

                env.dynamic_cosmosdb_primary_master_key = sh(
                  script:"""#!/bin/bash
                    terraform output cosmosdb_primary_master_key
                  """,
                  returnStdout: true,
                  label: "Terraform: Extract ComsosDB Primary Master Key"
                )

                env.dynamic_app_insights_instrumentation_key = sh(
                  script:"""#!/bin/bash
                    terraform output app_insights_instrumentation_key
                  """,
                  returnStdout: true,
                  label: "Terraform: Extract Application Insights Key"
                )
              }

            }
          }

        } // End of AppInfraDev

        stage("DeployDev") {
          agent {
            docker {
              image "amidostacks/ci-k8s:0.0.11"
            }
          }

          environment {
            cat_template_output="false"
            additional_args="-no-empty"
            // App Variables
            resource_def_name="stacks-java-api-jenkins"
            namespace="${environment_name}-java-api-jenkins"
            aks_cluster_resourcegroup="${core_resource_group}"
            aks_cluster_name="amido-stacks-nonprod-eun-core"
            app_name="java-api-jenkins"
            version="${dynamic_docker_image_tag}"
            environment="${environment_name}"
            tls_domain="${base_domain_nonprod}"
            aadpodidentitybinding="stacks-webapp-identity" // Unused currently
            k8s_image="${k8s_docker_registry_nonprod}/${docker_image_name}:${dynamic_docker_image_tag}"
            log_level="Debug"
            // Template 1
            k8s_deploy_template_1="api-deploy.yml"
            base_k8s_deploy_template_1="${self_repo_k8s_src}/app/base_${k8s_deploy_template_1}"
            output_k8s_deploy_template_1="${self_repo_k8s_src}/app/${k8s_deploy_template_1}"
            // Deployment 1
            deployment_name_1="deploy/${resource_def_name}"
            deployment_namespace_1="${namespace}"
            deployment_timeout_1="120s"
            // Terraform outputs from AppInfraDev
            cosmosdb_endpoint="${dynamic_cosmosdb_endpoint}"
            cosmosdb_key="${dynamic_cosmosdb_primary_master_key}"
            app_insights_key="${dynamic_app_insights_instrumentation_key}"
          }

          steps {
            // Copy this for each template you may have changing:
            // `base_k8s_deploy_template_1` to another template
            // and `output_k8s_deploy_template_1` to another template
            sh(
              script: """#!/bin/bash
                bash "${dynamic_build_scripts_directory}/deploy-k8s-envsubst.bash" \
                  -a "${base_k8s_deploy_template_1}" \
                  -b "${additional_args}" \
                  -Y "${cat_template_output}" \
                  -Z "${output_k8s_deploy_template_1}"
              """,
              label: "K8s: Yaml (${output_k8s_deploy_template_1})"
            )
            /////

            withCredentials([
              string(credentialsId: 'NONPROD_AZURE_CLIENT_ID', variable: 'NONPROD_AZURE_CLIENT_ID'),
              string(credentialsId: 'NONPROD_AZURE_CLIENT_SECRET', variable: 'NONPROD_AZURE_CLIENT_SECRET'),
              string(credentialsId: 'NONPROD_AZURE_TENANT_ID', variable: 'NONPROD_AZURE_TENANT_ID'),
              string(credentialsId: 'NONPROD_AZURE_SUBSCRIPTION_ID', variable: 'NONPROD_AZURE_SUBSCRIPTION_ID')
            ]) {
              sh(
                script: """#!/bin/bash
                  bash "${dynamic_build_scripts_directory}/util-azure-login.bash" \
                    -a "${NONPROD_AZURE_CLIENT_ID}" \
                    -b "${NONPROD_AZURE_CLIENT_SECRET}" \
                    -c "${NONPROD_AZURE_TENANT_ID}" \
                    -d "${NONPROD_AZURE_SUBSCRIPTION_ID}"
                """,
                label: "Login: Azure CLI"
              )
            }

            sh(
              script: """#!/bin/bash
                bash "${dynamic_build_scripts_directory}/util-azure-aks-login.bash" \
                  -a "${aks_cluster_resourcegroup}" \
                  -b "${aks_cluster_name}"
              """,
              label: "Login: Azure AKS Cluster Login"
            )

            // Copy this for each template you may have changing:
            // `output_k8s_deploy_template_1` to another template
            sh(
              script: """#!/bin/bash
                bash "${dynamic_build_scripts_directory}/deploy-k8s-apply.bash" \
                  -a "${output_k8s_deploy_template_1}"
              """,
              label: "Deploy: Kubectl Apply (${output_k8s_deploy_template_1})"
            )
            /////

            // Copy this for each template you may have changing:
            // `deployment_name_1` to another deployment,
            // `deployment_namespace_1` to another namespace for the new deployment
            // and `deployment_timeout_1` for how much time you want to wait for the app to deploy
            sh(
              script: """#!/bin/bash
                bash "${dynamic_build_scripts_directory}/deploy-k8s-rollout-status.bash" \
                  -a "${deployment_name_1}" \
                  -b "${deployment_namespace_1}" \
                  -Z "${deployment_timeout_1}"
              """,
              label: "Deploy: Kubectl Rollout Status Check (${deployment_name_1} @ ${deployment_namespace_1})"
            )
            /////

          }

        } // End of DeployDev

        stage("PostDeployDev") {
          agent {
            docker {
              image "azul/zulu-openjdk-debian:11"
            }
          }

          steps {
            dir("${self_functional_testproject_dir}") {
              // Copy this for each tag you have, for example @Functional and @Smoke etc.
              // Note: Don't forget to update the `maven_allowed_post_deploy_test_tags` in the root pipeline file.
              sh(
                script: """#!/bin/bash
                  bash "${dynamic_build_scripts_directory}/test-maven-post-deploy-tagged-test-run.bash" \
                    -a "@Functional" \
                    -b "${functional_test_base_url}" \
                    -Y "${maven_ignored_post_deploy_test_tags}" \
                    -Z "${maven_cache_directory}"
                """,
                label: "Post-Deploy Test: Run Functional Tests"
              )

              sh(
                script: """#!/bin/bash
                  bash "${dynamic_build_scripts_directory}/test-maven-serenity-aggregate.bash" \
                    -Z "${maven_cache_directory}"
                """,
                label: "Post-Deploy Test: Serenity Report Aggregate"
              )

              sh(
                script: """#!/bin/bash
                  bash "${dynamic_build_scripts_directory}/test-maven-post-deploy-test-verify.bash" \
                    -Z "${maven_cache_directory}"
                """,
                label: "Post-Deploy Test: Verify Test Run"
              )
            }
          }

          post {
            always {
              dir("${self_functional_testproject_dir}") {
                // Publish Test Results
                junit "${maven_post_deploy_failsafe_reports_directory}/*.xml"

                // Publish Serenity Report
                publishHTML (
                  target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: "${maven_post_deploy_html_report_directory}",
                    reportFiles: "**/*",
                    reportName: "${environment_name} - Serenity Report",
                    reportTitles: "${environment_name} - Serenity Report"
                  ]
                )
              }
            }
          } // post end

        } // End of PostDeployDev

      } // End of Dev Stages

    } // End of Dev Stage

  } // End of Stages

} // End of Pipeline
