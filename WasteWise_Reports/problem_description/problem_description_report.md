---
type: article
title: "Everything WasteWise: A Unified Waste Management System"
date: 2024-09-11T14:46:33Z
draft: false # set to false if your team gives consent to publish this post publicly
---

<!-- TOC start (generated with https://github.com/derlin/bitdowntoc) -->

## Table of Contents

- [Table of Contents](#table-of-contents)
- [1. Problem Description](#1-problem-description)
  - [1.1 Analysis of Existing Solutions](#11-analysis-of-existing-solutions)
  - [1.2 Shortcomings of Existing Solutions](#12-shortcomings-of-existing-solutions)
  - [1.3 Stakeholder Analysis](#13-stakeholder-analysis)
  - [1.4 Ethical Considerations](#14-ethical-considerations)
    - [Data Security](#data-security)
    - [Accuracy of Information](#accuracy-of-information)
    - [User Verification](#user-verification)
  - [1.5 Sources of Ambiguity](#15-sources-of-ambiguity)
    - [Clarity on Goal of the System](#clarity-on-goal-of-the-system)
    - [Compliance with Dutch and EU Regulations](#compliance-with-dutch-and-eu-regulations)
    - [Context-Specific Term Definitions](#context-specific-term-definitions)
- [2. Solution Design](#2-solution-design)
  - [2.1 Proposed Solution](#21-proposed-solution)
  - [2.2 Benefits of Our Solution](#22-benefits-of-our-solution)
  - [2.3 Quality Attributes](#23-quality-attributes)
  - [2.4 Scenarios](#24-scenarios)
  - [2.5 Initial Concept](#25-initial-concept)
    - [Technical Dependencies](#technical-dependencies)
    - [Organizational Dependencies](#organizational-dependencies)
- [3. Proposed Roadmap](#3-proposed-roadmap)
- [4. Revenue Model](#4-revenue-model)
- [References](#references)
- [Appendix A: Requirements](#appendix-a-requirements)
- [Appendix B: Sequence Diagrams for User Scenarios](#appendix-b-sequence-diagrams-for-user-scenarios)
- [Appendix C: Acknowledgments](#appendix-c-acknowledgments)
  - [Wildfires Project](#wildfires-project)

<!-- TOC end -->

<!-- TOC --><a name="1-problem-description"></a>

## 1. Problem Description

Have you ever waited for days for your waste to be collected? Found that the recycling bin in your neighborhood was removed, forcing you to walk 15 minutes to the next nearest bin? Had doubts about which bin to use to dispose of your waste? Although there are systems in place that offer information on waste disposal in different European Union (EU) countries, such systems are managed by private waste management companies rather than a governmental body. Hence, these systems do not provide information about regions that the company does not operate in. Therefore, a unified system covering all aspects of waste management is imperative. We propose **WasteWise** to provide this unification initially on a national scale, and later on the EU scale.

**WasteWise's** mission is to develop an integrated recycling information platform with educational resources, communication channels for citizens, and collaboration mediums for governmental organizations and private waste management companies.

<!-- TOC --><a name="11-analysis-of-existing-solutions"></a>

### 1.1 Analysis of Existing Solutions

Before proposing a solution to the problem at hand, we conducted an analysis of several existing systems focused on waste collection information. Our goal was to identify areas for improvement and understand where these systems fall short in addressing the problem effectively. For analysis, we chose three systems from the Netherlands, namely [Avalex](https://www.avalex.nl/), [HVC](https://www.hvcgroep.nl/), and [Cure afvalbeheer](https://www.cure-afvalbeheer.nl/) since our initial prototype would concern the Netherlands and only then have the possibility to be scaled up. The other three systems are [BSR](https://www.bsr.de/index.php), [Town of Vienna](https://www.viennava.gov/residents/town-services/trash-and-recycling), and [Moji odpadki](https://www.mojiodpadki.si/), which are the waste management systems responsible for Berlin, Vienna, and Ljubljana. We chose to investigate these systems because Germany, Austria, and Slovenia have the highest municipal waste recycling rate in Europe, according to the European Environment Agency [1]. The identified features and limitations for each system can be seen in the table below.

![Existing Solutions Table](WasteWise_Reports/problem_description/images/existing_solutions-table.png)
**Table 1:** Existing Solutions

Deriving from Table 1, the common shortcomings of the existing systems are explained in the following section.

<!-- TOC --><a name="12-shortcomings-of-existing-solutions"></a>

### 1.2 Shortcomings of Existing Solutions

**Difficulty in Locating Certain Areas and Addresses:** Municipalities are responsible for assigning collectors to different locations, and sometimes an existing recycling system does not operate in a certain area. When this happens, information on alternatives is unclear and messages are often vague. If a type of waste collection is unavailable, there's no guidance to the nearest collection point, making proper disposal harder for citizens.

**Inconsistent Platform Interfaces:** Navigation and information display varies on different platforms, for example when accessing a calendar for a given address. This makes it time-consuming for users to adapt to unfamiliar systems and it can easily become confusing, especially for people visiting or moving to a different region/country.

**Lack of English Alternatives:** Navigating those platforms is significantly more difficult since only some of the instructions can be automatically translated by the browser. Moreover, none of the mobile applications corresponding to these systems have an English alternative, making them completely inaccessible to non-native users.

<!-- TOC --><a name="13-stakeholder-analysis"></a>

### 1.3 Stakeholder Analysis

After doing research on existing solutions, we can analyze and understand the expectations of the involved stakeholders. Correctly analyzing the stakeholders' needs is essential for deriving the use cases, scenarios, and requirements of the system [2]. Mendelow's matrix is used for stakeholder analysis because it is a widely used approach to categorize stakeholders concerning the trade-off between power/influence and interest/importance [3].

![Stakeholder Matrix](WasteWise_Reports/problem_description/images/stakeholder_matrix_updated.png)  
**Figure 1:** Stakeholder Matrix

**Manage Closely:** The stakeholders in this quadrant are our priority, as they will be the primary users of the system. These stakeholders have a high interest in using our system. Moreover, their needs and expectations will have a significant influence on its features and operations. In our case, this quadrant comprises _Waste Collectors_ and _Waste Processing Facilities_. Waste Collectors can create a request for a certain amount of waste to be processed by a given date, while Processing Facilities can respond by posting the quantity of a specific waste type they can process. _Local Governmental Authorities_ refer to Municipal agencies, which have the main responsibility of overseeing waste management processes in their region and thus, besides having a high authority due to their governmental roles, they also have a high interest in our system to handle proper waste management.

**Keep Satisfied:** We will ensure that the features offered by our system comply with the waste management regulations of the _Netherlands Government_. The Government has power as the primary authority and the regulator of the system, although its interest in its operational details is limited. Once we scale the system to the EU, the _European Union_ will take this role and its regulations for recycling and waste management will apply.

**Keep Informed:** These stakeholders will be kept well-informed of changes in the system and will be consulted for advice. Civilians are the first stakeholder in this quadrant. They will use our system to gain accurate information about good recycling practices, learn about the waste pickup schedule for their neighborhood, and be informed about the nearest recycling bin locations. Although _Civilians_ do not have significant influence over the system's features, they have a high interest in using our system to gain proper recycling information. Bulk waste generators such as _Market Chains and Restaurants_ are also interested in effective waste management and knowing the waste pickup schedule. These waste generators provide valuable data about the volume of waste that needs to be collected and processed, making their collaboration and input on waste collection essential. _Developers_ offer technical expertise throughout the system development process; therefore, keeping them informed of new system features and functionality is crucial.

**Monitor:** Although these stakeholders have limited influence on the system, their collaboration is important. Therefore, monitoring these stakeholders’ activities and opinions is required. _Media Outlets_ influence public attitudes, which makes their cooperation in promoting our system and good recycling practices crucial for the system’s success. _Local Communities and Neighborhood Associations_ represent civilian interests and can advocate for better waste management practices. Therefore, similar to Media Outlets, the cooperation of these communities is essential for having an informed user base.

<!-- TOC --><a name="14-ethical-considerations"></a>

### 1.4 Ethical Considerations

As WasteWise architects, we hold ethical responsibility toward the listed stakeholders. We have identified three main sources of ethical concern that should be addressed before designing our system:

<!-- TOC --><a name="data-security"></a>

#### Data Security

Waste Collectors and Waste Processing Facilities will share company-related information so that they can be verified and included in our system. Since we propose to scale the system to the EU, compliance with the EU’s regulation on information privacy is required [4]. Therefore, our system will comply with the General Data Protection Regulation (GDPR) law. Furthermore, civilians accessing the system for neighborhood-specific information will only have to provide their postcode, and this data will not be persisted. This is an intentional choice to not keep any unnecessary personal information in our database and hence avoid conflicts with privacy regulations. The system does persist data regarding companies and their listings, but this data is expected to be public and will therefore not necessitate security.

<!-- TOC --><a name="accuracy-of-information"></a>

#### Accuracy of Information

Information reliability is a concern as our system aims to promote environmental responsibility among its users and to ensure reliable communication between parties regarding the availability of their services. This requires our system to provide accurate information on recycling processes, waste collection schedules, and available facilities. Information reliability should be consistently regulated by the government and municipalities. Regular checks should be conducted on the sources of recycling information offered by the system, as well as on the vacancies posted by the facilities to ensure that reliable information is conveyed to the users.

<!-- TOC --><a name="user-authentication"></a>

#### User Verification

The service providers who sign up to our system should be verified before they can post listings to ensure they are legitimate and not harmful. We will conduct this verification by constructing a 3-tier administrative system. These tiers will be the EU, governments, and municipalities in decreasing order of authority. Every level will be responsible for verifying users from a lower level and the lowest level, which is the municipalities, will verify the sign-up requests from service providers under their jurisdiction.

<!-- TOC --><a name="15-sources-of-ambiguity"></a>

### 1.5 Sources of Ambiguity

A robust system design requires addressing potential sources of ambiguity and navigating them before implementing a solution [5]. The sources of ambiguity that most impact our system design are identified and explained below.

#### Clarity on Goal of the System

The EU-wide system for waste management is a complex problem facing multiple stakeholders, which results in a massive scope. We have derived [requirements](#appendix-a-requirements) to specify our goal. Our main focus is on providing a platform for waste management services to be exchanged, and on enhancing the clarity of waste disposal for households and small businesses.

<!-- TOC --><a name="compliance-with-dutch-and-eu-regulations"></a>

#### Compliance with Dutch and EU Regulations

WasteWise will initially be used in the Netherlands, with the goal of becoming a EU-wide system. When the system is scaled for use in the EU, compliance must be readjusted to take both state-level regulations as well as common regulations mandated by the EU. In this report, we focus on the initial product and will therefore consider the Netherlands as the use domain.

The Netherlands has devised [Web Guidelines](https://www.government.nl/accessibility) list based on design rules recognized by the World Wide Web Consortium (W3C) [6]. The guidelines are mandatory to incorporate for every government system used by the general public. Therefore, it will act as a baseline for accessibility and sustainability levels to be achieved by WasteWise.

<!-- TOC --><a name="context-specific-term-definitions"></a>

#### Context-Specific Term Definitions

Below we clarify domain-specific terminology, that may seem ambiguous, to prevent misunderstandings.

- **Waste Management:** This phrase is used to cover all policies and practices in place with regard to the collection and processing of every type of waste.
- **Recycling:** In this report, recycling refers to the process of converting waste into new materials and objects by making it require the properties it had in its original state [7]. It does not include energy generation through the combustion of waste.
- **Service Provider:** This phrase is used throughout the report to collectively refer to waste collectors and processors. Depending on the country/region, these parties could be the municipality itself or private companies that they have contracted. These constituents are explained separately below:

  - **Waste Collector:** Refers to a party whose duty is to collect household/small business waste and deliver it to the facility where it will be processed.
  - **Waste Processor:** Refers to a party that intakes a certain type of waste and processes it, either through recycling, energy production, or landfilling.

- **Listing:** Refers to an advert that can be posted by a _service provider_ on our system regarding a vacancy in their schedules. An example listing from a processor can be: _"400 Tonnes of PET plastic bottles can be recycled to an 80% efficiency between 5-10th of October 2024. Service Price: 4000 EUR."_.

<!-- TOC --><a name="2-solution-design"></a>

## 2. Solution Design

<!-- TOC --><a name="21-proposed-solution"></a>

### 2.1 Proposed Solution

The system we propose aims to be an end-to-end platform for processes related to waste information and management. To better understand the user's perspective, the actions supported by our system are defined based on the [needs of the stakeholders](#13-stakeholder-analysis) and the [defined requirements](#appendix-a-requirements), and visualized in the use case diagram below. The role specifications of the actors in the use case diagram can be found in [Appendix A](#appendix-a-requirements).

![Use Case Diagram](WasteWise_Reports/problem_description/images/use_case_diagram_updated.png)  
**Figure 2:** Use Case Diagram

<!-- TOC --><a name="22-benefits-of-our-solution"></a>

### 2.2 Benefits of Our Solution

By offering an integrated waste management system with educational resources, communication channels for citizens, and collaboration mediums for governmental organizations and private waste management companies, we aim to overcome the [shortcomings of the existing systems](#12-shortcomings-of-existing-solutions), providing the following benefits:

- **Unifying various trash processing procedures into a single platform:** The system brings together waste producers (civilians and market-restaurant chains), private waste management companies, and municipalities. It also synchronizes and displays information about trash pickup schedules, bin locations, and available processing capacities collected from various waste collectors, processing facilities, and municipalities, all on a single platform.

- **Helping citizens who have just moved find relevant information wherever they are in Europe:** The platform allows access to recycling information specific to the user's location, taking into consideration that each country has its own regulations, processes, and guidelines for waste management. The user will be able to view a timetable of trash pickups and a map showing the location of the garbage bins for their area.

- **Connecting waste collectors and processors:** Integrating a communication medium, the system offers a collaborative environment where waste collectors and processors from both the public and private sectors can exchange vacancy information. Processing facilities will be able to post their vacancies, specifying that they can “process _A_ amount of material _B_ between the dates _C_ and _D_”. Likewise, waste collectors will be able to post a need for a certain amount of a type of waste to be processed until a specific date. This will allow for enhanced communication and easier delegation of work among the involved parties.

- **A communication platform for everyone:** By offering a form submission functionality, our system enables users to send complaints and requests to their local authorities about waste disposal. This way, everyone can easily share their concerns and feedback with the officials.

<!-- TOC --><a name="23-quality-attributes"></a>

### 2.3 Quality Attributes

In order to design a suitable system to solve the identified issues, we have analyzed the [needs of the stakeholders](#13-stakeholder-analysis), as well as the [defined requirements](#appendix-a-requirements). This helped us select appropriate quality attributes that will represent the focus of our efforts and the starting point for the decisions taken in designing the rest of the architecture. The selection includes ten main attributes categorized by the ISO 25010 Quality Characteristics [8], and can be visualized in the table below.

![Main Quality Attributes to Consider](WasteWise_Reports/problem_description/images/quality_attributes_table.png)  
**Table 2:** Considered Quality Attributes

In order to assess the priority of these attributes, and decide what we need to focus on and analyze further, we have conducted a prioritization process. This was done by means of Brosseau's spreadsheet [9], by comparing each pair of quality attributes and deciding which one is more valuable for our system. For each pair of attributes, the arrow points to the one with higher priority. The final score represents the importance for each attribute, where a higher score means higher priority. The table and the associated results can be seen below.

![Quality attribute prioritization using Brosseau's spreadsheet. For each pair of attributes, the arrow points to the one with higher priority. The final score corresponds to the importance of each attribute.](WasteWise_Reports/problem_description/images/brosseau_spreadsheet.png)  
**Table 3:** Results of Quality Attribute Prioritization

The analysis shows that the most critical quality attributes for our system are **Scalability**, **Modularity**, **Confidentiality**, and **Functional Correctness**. Given the importance of these attributes, we need to be aware of the implications they bring. To this end, we have analyzed the trade-offs, associated risks and possible mitigations associated with each attribute, which are summarized below.

![Trade-Offs, Risks, and Possible Mitigations for Prioritized Quality Attributes](WasteWise_Reports/problem_description/images/tradeoffs_table.png)  
**Table 4:** Trade-offs, Risks, and Possible Mitigations

<!-- TOC --><a name="24-scenarios"></a>

### 2.4 Scenarios

Following the stakeholder and requirements analyses, we devised user scenarios for our most essential stakeholders. We decided to outline user scenarios to present the functionality of our system from the stakeholders' perspectives and to visualize the user interactions with our system. The scenario-based design allowed us to describe the usage of the envisioned system early on in development [10], informing domain modeling and the upcoming architectural design process. The overview of the user scenarios is displayed below. The complete definition of each scenario, including a sequence diagram and background information, is presented in [Appendix B](#appendix-b-sequence-diagrams-for-user-scenarios). The role specifications of the actors can be found in [Appendix A](#appendix-a-requirements).

**Scenario 1:** A Visitor selects their country and postal code to view waste collection information.  
**Scenario 2:** A Visitor checks the nearest bin locations in their neighborhood.  
**Scenario 3:** A Visitor submits a complaint form.  
**Scenario 4:** A Visitor submits a bulk waste collection request form.  
**Scenario 5:** A Governmental User verifies a Private Owner.  
**Scenario 6:** A Private Owner logs in to the system.  
**Scenario 7:** A Private Owner views the available listings.  
**Scenario 8:** A Private Owner applies for an account.  
**Scenario 9:** A Visitor selects their country and postal code to view their waste pickup schedule.  
**Scenario 10:** A Governmental User creates an account for a Private Owner.  
**Scenario 11:** A Governmental User checks the permission level of another Governmental User.  
**Scenario 12:** A Governmental User updates the admin status of another Governmental User.  
**Scenario 13:** A Private Owner creates a new listing.  
**Scenario 14:** A Private Owner deletes one of their existing listings.  
**Scenario 15:** A Private Owner edits one of their existing listings.  
**Scenario 16:** A Private Owner views the profile of another Private Owner.  
**Scenario 17:** A Governmental User can view the forms sent to them.

<!-- TOC --><a name="25-initial-concept"></a>

### 2.5 Initial Concept

In this section, we present a high-level overview of how our system will integrate the different domains and dependencies. The bounded context diagram in Figure 3 showcases the division of our system into smaller subdomains, represented by rectangles. The contexts were derived from [scenarios](#24-scenarios), and each has its own logic and responsibilities.

![Bounded Context Diagram](WasteWise_Reports/problem_description/images/bounded-contexts-diagram-updated.png)  
**Figure 3:** Bounded Context Diagram

<!-- TOC --><a name="technical-dependencies"></a>

Additionally, we have identified external dependencies that our system will rely on, as illustrated in the system context diagram in Figure 4 and explained further in the following Technical Dependencies section.

![System Context Diagram](WasteWise_Reports/problem_description/images/system-context-diagram.jpeg)  
**Figure 4:** System Context Diagram

#### Technical Dependencies

**Auth0:** Allows for integrating APIs for permission management and 2-factor authentication [11,12]. Since our users include Governmental users from various countries (see [Scenarios](#24-scenarios)) and Private Owners, it is crucial to ensure role-based access control and secure access to sensitive data (see [Ethical Considerations](#14-ethical-considerations)).

**Google Maps API:** Allows for the address validation and for the possible display of trash pickup points [13]. It is relevant for Scenarios 1 and 2.

**Email Service:** Integrates email protocols for sending and receiving emails. Submitted forms by users will be sent by email to governmental agencies.

<!-- TOC --><a name="organizational-dependencies"></a>

#### Organizational Dependencies

The system involves collaboration between governmental users and private service providers (see [Stakeholder Analysis](#13-stakeholder-analysis)). Without their participation, the platform cannot function as intended. Governmental agencies are responsible for verifying external companies, and the system is dependent on municipal agencies for schedule collection and form handling. Private owners are responsible for creating reliable listings. Due to legal and ethical reasons, the platform will need to comply with various local, national, and European regulations (see [Ethical Considerations](#14-ethical-considerations) and [Sources of Ambiguity](#15-sources-of-ambiguity)).

The Domain Diagram below further visualizes relationships between entities. It simplifies the real-world scenario described in [Problem Analysis](#1-problem-description), and ensures that all needs and behaviors are captured. With blue arrows we acknowledge organizational dependencies of our system. This entity-relationship diagram, together with the bounded context diagram, give a blueprint for our future system architecture.

![Domain Diagram](WasteWise_Reports/problem_description/images/domain_diagram.png)  
**Figure 5:** Domain Model

The dependencies above are important to consider when deriving an appropriate revenue model, as they require man-hours to build and maintain the system. In order to outline the estimated time consumption of various tasks associated with the system creation, the next section proposes a _Roadmap_ before delving into the details of funding.

<!-- TOC --><a name="3-proposed-roadmap"></a>

## 3. Proposed Roadmap

Our development roadmap outlines the phased approach for implementing and enhancing the features of WasteWise.

![Proposed Roadmap](WasteWise_Reports/problem_description/images/roadmap_v2.png)  
**Figure 6:** Proposed Roadmap

<!-- TOC --><a name="4-revenue-model"></a>

## 4. Revenue Model

Since the primary purpose of our proposed recycling information system is to contribute to sustainability and promote public awareness about waste management, we foresee funding from the public sector. By relying on governmental funding, we aim to preserve the long-term viability of our system rather than its profitability. During the launch phase, our initial revenue stream will be sponsorship from the Netherlands government, municipalities, or other beneficiaries, as our initial prototype will only be operational in the Netherlands. We aim to receive yearly public funding to increase public welfare by improving waste management across the country.

During the scaling phase, we plan to apply for the [LIFE Programme](https://cinea.ec.europa.eu/programmes/life_en), which is the EU’s funding instrument for the environment and climate action [14]. With this funding, we aim to extend our annual revenue streams to cover the increasing operational costs that arise from the scale-up.

<!-- TOC --><a name="references"></a>

## References

[1] **Municipal Waste Recycling Rates**. “Municipal waste recycling rates in Europe by country,” Europa.eu, Dec. 19, 2023. https://www.eea.europa.eu/en/analysis/maps-and-charts/municipal-waste-recycled-and-composted-7 (accessed Sep. 21, 2024).

[2] **Stakeholder Analysis**. L. W. Smith, “Stakeholder analysis: A pivotal practice of successful projects,” Project Management Institute, Sep. 07, 2020. https://www.pmi.org/learning/library/stakeholder-analysis-pivotal-practice-projects-8905 (accessed Sep. 30, 2024).

[3] **Mendelow Matrix**. K. Ginige, D. Amaratunga, and R. Haigh, “Mapping stakeholders associated with societal challenges: A Methodological Framework,” Procedia Engineering, vol. 212, pp. 1195–1202, 2018, doi: https://doi.org/10.1016/j.proeng.2018.01.154.

[4] **GDPR**. B. Wolford, “What is GDPR, the EU’s new data protection law?,” GDPR.eu, 2024. https://gdpr.eu/what-is-gdpr/

[5] **Sources of Ambiguity**. V. Gervasi, A. Ferrari, D. Zowghi, and P. Spoletini, “Ambiguity in Requirements Engineering: Towards a Unifying Framework,” ResearchGate, Oct. 09, 2019. https://www.researchgate.net/publication/336351565_Ambiguity_in_Requirements_Engineering_Towards_a_Unifying_Framework (accessed Oct. 01, 2024).

[6] **Accessibility**. “Accessibility,” Government.nl, 2015. https://www.government.nl/accessibility (accessed Sep. 23, 2024).

[7] **Recycling**. G. Villalba, M. Segarra, A.I. Fernández, J.M. Chimenos, F. Espiell, "A proposal for quantifying the recyclability of materials," Resources, Conservation and Recycling. Dec. 2002. https://ui.adsabs.harvard.edu/abs/2002RCR....37...39V

[8] **ISO 25010 Quality Characteristics**. “ISO/IEC 25010,” ISO25000: Software and Data Quality. https://iso25000.com/index.php/en/iso-25000-standards/iso-25010

[9] **Brosseau's Spreadsheet**. K. Wiegers and J. Beatty, Software Requirements, 3rd ed. Microsoft Press, 2013. Accessed: Oct. 04, 2024. [Online]. Available: https://www.booksfree.org/wp-content/uploads/2022/03/Software_Requirements_3rd_Edition_compressed.pdf

[10] **Scenario-Based Design**. J. Jacko and A. Sears, "Scenario-Based Design," in _The Human-Computer Interaction Handbook: Fundamentals, Evolving Technologies and Emerging Applications_, Lawrence Erlbaum Associates, 2002, pp. 1032-1050.

[11] **Auth0 API Permissions**. Auth0, “Add API Permissions,” Auth0 Docs, 2024. https://auth0.com/docs/get-started/apis/add-api-permissions (accessed Sep. 30, 2024).

[12] **Auth0 MFA API**. Auth0, “Auth0 MFA API,” Auth0 Docs, 2024. https://auth0.com/docs/secure/multi-factor-authentication/multi-factor-authentication-developer-resources/mfa-api (accessed Sep. 30, 2024).

[13] **Google Maps API**. “Address Validation API overview,” Google Developers. https://developers.google.com/maps/documentation/address-validation/overview (accessed Sep. 30, 2024).

[14] **LIFE Programme**. European Commission, “LIFE Programme,” European Climate, Infrastructure and Environment Executive Agency. https://cinea.ec.europa.eu/programmes/life_en (accessed Sep. 21, 2024).

<!-- TOC --><a name="appendix-a-requirements"></a>

## Appendix A: Requirements

We have derived functional requirements to better understand the project's scope and ease the development process.

**Roles Specification:**

- **Visitors:** Stakeholders not involved in the collection, processing, or governance of recycling. Examples include civilians and market chain owners.
- **Governmental Users (Admins):** Individuals involved in governmental work, such as municipalities or the EU Parliament, who are responsible for managing the platform and verifying waste collection and processing facilities.
- **Private Owner:** Companies involved in waste collection or processing. There are two types of Private Owners: Waste Collectors and Processing Facilities.

**Functional Requirements:**

**MUST:**

- As a Visitor, I must be able to input my country and postal code to view information regarding waste collection in my area. This information must include:
  - A schedule for waste collection.
  - An explanation of how recycling works in my region/country.
- As a Private Owner, I must be able to log into my account, which contains information about my company.
- As a Private Owner, I must be able to create listings regarding the availability of my services so that other Private Owners can contact me if they want to use them.
- As a Private Owner, I must be able to search through listings of other Private Owners. For example, if a processing company has a surplus of waste and cannot process it on time, they could search for a listing of another company that offers to process that waste.
- As a Private Owner, I must be able to have multiple listings to the listings platform.
- As a Governmental User, I must be able to create accounts for Private Owners after they are verified through a municipality.
- As a Governmental User, I must be able to view submitted forms by visitors and private owners.
- The system must allow for different permission levels regarding Governmental Users. Some Governmental Users must only be able to make changes to data within their country/municipality region.

**SHOULD:**

- As a Visitor, I should be able to view pickup points for waste collection displayed on a map.
- As a Visitor, it should be clear to me if I input a wrong address (an address that does not exist).
- As a Visitor, I should be able to fill out a contact form to:
  - Request a bulky waste collection.
  - Raise a complaint.
- As a Private Owner, I should be able to fill out a verification form to start the verification process as a processing or collecting facility.
- As a Private Owner, I should be able to apply various filters when searching for listings of other Private Owners. Filters should include:
  - Specifying whether I am looking for a collecting or a processing facility.
  - Regional, national, or European scale operation.
  - Specific company names.
- As a Governmental User, I should be able to edit a verification date of a Private Owner and display it on their profile.
- As a Governmental User, I should be able to delete accounts for Private Owners.
- As a Governmental User, I should be able to view the forms sent to me.

**COULD:**

- As a Visitor, I could view recycling statistics for a specific region or a country.
- As a Visitor, I could visually track the recycling process in my region.
- As a Private Owner, I could apply more filters when searching for another Private Owner's listings, such as the type of material (glass, paper, plastic, etc.)
- As a Private Owner, I could view statistics about my company.
- As a Private Owner, I could report another Private Owner.

**WON'T:**

- As a Visitor, I won't be able to view events related to recycling in my region.
- As a Visitor, I won't be able to chat on a forum with other visitors or private owners.
- As a Private Owner, I won't be able to rate other Private Owners.
- The System won't provide a communication platform between different Private Owners. Instead, it will display contact information so they can reach out to each other outside the platform.

<!-- TOC --><a name="appendix-b-sequence-diagrams-for-user-scenarios"></a>

## Appendix B: Sequence Diagrams for User Scenarios

**Scenario 1:** A Visitor selects their country and postal code to view waste collection information.  
**Background:** Julie has just moved to the Netherlands from her home country. She wants to know the waste collection practices in her area.  
**Sequence Diagram:** The following diagram displays the flow of this scenario. It clearly displays the steps that are to be followed for a Visitor to check country-specific waste collection information.

![Sequence Diagram for User Scenario 1](WasteWise_Reports/problem_description/images/sequence_diagram_US1.drawio.png)  
**Figure 7:** Sequence Diagram for User Scenario 1

**Scenario 2:** A Visitor checks the nearest bin locations in their neighborhood.  
**Background:** Harry has moved to a new neighborhood and does not know where to dispose of his waste. He wants to learn about the nearest bin locations in the neighborhood.  
**Sequence Diagram:** The following diagram displays the flow of this scenario. It clearly displays the steps that are to be followed for a Visitor to check the nearby bin locations.

![Sequence Diagram for User Scenario 2](WasteWise_Reports/problem_description/images/sequence_diagram_US2.drawio.png)  
**Figure 8:** Sequence Diagram for User Scenario 2

**Scenario 3:** A Visitor submits a complaint form.  
**Background:** Dalimir, a local resident, is frustrated because a nearby plastic recycling bin has been moved 10 km's away, making recycling more difficult for him.  
**Sequence Diagram:** The following diagram displays the flow of this scenario. It clearly displays the steps that are to be followed for a Visitor to submit a complaint form to a municipality.

![Sequence Diagram for User Scenario 3](WasteWise_Reports/problem_description/images/sequence_diagram_US3.drawio.png)  
**Figure 9:** Sequence Diagram for User Scenario 3

**Scenario 4:** A Visitor submits a bulk waste collection request form.  
**Background:** Edward is a market chain owner. His employees have dumped bulk waste into the nearby trash bin. The bin requires an earlier collection because it is already full.  
**Sequence Diagram:** The following diagram displays the flow of this scenario. It clearly displays the steps that are to be followed for a Visitor to submit a bulk waste collection request to the waste collector of their neighborhood.

![Sequence Diagram for User Scenario 4](WasteWise_Reports/problem_description/images/sequence_diagram_US4.drawio.png)  
**Figure 10:** Sequence Diagram for User Scenario 4

**Scenario 5:** A Governmental User verifies a Private Owner.  
**Background:** Barbra is an administrator working at a municipal office responsible for waste management facilities. She is tasked with verifying new waste collection and processing companies as well as managing existing accounts.  
**Sequence Diagram:** The following diagram displays the flow of this scenario. It clearly displays the steps that are to be followed for a Governmental User to verify a new company that wants to sign up with WasteWise.

![Sequence Diagram for User Scenario 5](WasteWise_Reports/problem_description/images/sequence_diagram_US5.drawio.png)  
**Figure 11:** Sequence Diagram for User Scenario 5

**Scenario 6:** A Private Owner logs in to the system.  
**Background:** Company A, an energy and waste company, wants to log into the system to check the posted vacancies on the listings platform.  
**Sequence Diagram:** The following diagram displays the flow of this scenario. It clearly displays the steps that are to be followed for a Private Owner to log in to WasteWise.

![Sequence Diagram for User Scenario 6](WasteWise_Reports/problem_description/images/sequence_diagram_US6.drawio.png)  
**Figure 12:** Sequence Diagram for User Scenario 6

**Scenario 7:** A Private Owner views the available listings.  
**Background:** Company B, a waste collection company, has recently gathered an unusually large volume of waste. Their regular processing partner is unable to handle the excess due to capacity constraints. Consequently, Company B needs to find an alternative processing facility with available capacity to manage the overflow.  
**Sequence Diagram:** The following diagram displays the flow of this scenario. It clearly displays the steps that are to be followed for a Private Owner to view the available listings in the listings platform.

![Sequence Diagram for User Scenario 7](WasteWise_Reports/problem_description/images/sequence_diagram_US7.drawio.png)  
**Figure 13:** Sequence Diagram for User Scenario 7

**Scenario 8:** A Private Owner applies for an account.  
**Background:** Dorothy is the owner of his new Waste Processing Business in the Netherlands. She aspires to work on an international scale. To advertise his business, he desires to apply for an account on WasteWise.  
**Sequence Diagram:** The following diagram displays the flow of this scenario. It clearly displays the steps that are to be followed for a Private Owner to apply for an account on WasteWise.

![Sequence Diagram for User Scenario 8](WasteWise_Reports/problem_description/images/sequence_diagram_US8.drawio.png)  
**Figure 14:** Sequence Diagram for User Scenario 8

**Scenario 9:** A Visitor selects their country and postal code to view their waste pickup schedule.  
**Background:** Erik has moved to the Netherlands and would like to know the trash pickup schedule for his address.  
**Sequence Diagram:** The following diagram displays the flow of this scenario. It clearly displays the steps that are to be followed for a Visitor to view the waste pickup schedule for their neighborhood.

![Sequence Diagram for User Scenario 9](WasteWise_Reports/problem_description/images/sequence_diagram_US9.drawio.png)  
**Figure 15:** Sequence Diagram for User Scenario 9

**Scenario 10:** A Governmental User creates an account for a Private Owner.  
**Background:** John, who works for the Delft municipality, is tasked with adding a new Waste Collection Company, C, to the system that helps with trash pickup around the region.  
**Sequence Diagram:** The following diagram displays the flow of this scenario. It clearly displays the steps that are to be followed for a Governmental User to create an account for a Private Owner.

![Sequence Diagram for User Scenario 10](WasteWise_Reports/problem_description/images/sequence_diagram_US10.drawio.png)  
**Figure 16:** Sequence Diagram for User Scenario 10

**Scenario 11:** A Governmental User checks the permission level of another Governmental User.  
**Background:** Zofia works for the Dutch Government and has an overview of all Dutch municipality employees. She would like to ensure that their permission levels only allow them to access information for their regions.  
**Sequence Diagram:** The following diagram displays the flow of this scenario. It clearly displays the steps that are to be followed for a Governmental User to view the permission level of another Governmental User.

![Sequence Diagram for User Scenario 11](WasteWise_Reports/problem_description/images/sequence_diagram_US11.drawio.png)  
**Figure 17:** Sequence Diagram for User Scenario 11

**Scenario 12:** A Governmental User updates the admin status of another Governmental User.  
**Background:** Zofia works for the Dutch Government and has an overview of all Dutch municipality employees. A municipal employee, Margarita, is transferred from one municipality to another, requiring a change in her admin status. Zofia updates Margarita's profile with valid data.  
**Sequence Diagram:** The following diagram displays the flow of this scenario. It clearly displays the steps that are to be followed for a Governmental User to update the admin status of another Governmental User that is lower in permission level.

![Sequence Diagram for User Scenario 12](WasteWise_Reports/problem_description/images/sequence_diagram_US12.drawio.png)  
**Figure 18:** Sequence Diagram for User Scenario 12

**Scenario 13:** A Private Owner creates a new listing.  
**Background:** Company D is a small Waste Processing Company that specializes in processing glass and has a spare vacancy. Thus, they would like to let other private owners know so that they can collaborate if needed.  
**Sequence Diagram:** The following diagram displays the flow of this scenario. It clearly displays the steps that are to be followed for a Private Owner to add a new listing.

![Sequence Diagram for User Scenario 13](WasteWise_Reports/problem_description/images/sequence_diagram_US13.drawio.png)  
**Figure 19:** Sequence Diagram for User Scenario 13

**Scenario 14:** A Private Owner deletes one of their existing listings.  
**Background:** Company E is a Waste Processing Facility, and they have reached their maximum capacity for waste processing. Thus, they would like to delete one of the listings that advertise their vacancy.  
**Sequence Diagram:** The following diagram displays the flow of this scenario. It clearly displays the steps that are to be followed for a Private Owner to delete an existing listing.

![Sequence Diagram for User Scenario 14](WasteWise_Reports/problem_description/images/sequence_diagram_US14.drawio.png)  
**Figure 20:** Sequence Diagram for User Scenario 14

**Scenario 15:** A Private Owner edits one of their existing listings.  
**Background:** Company F is a Waste Processing Facility that recently got equipped to process a new resource—plastic. Thus, they would like to update their listings regarding their vacancies to include plastic processing.  
**Sequence Diagram:** The following diagram displays the flow of this scenario. It clearly displays the steps that are to be followed for a Private Owner to edit an existing listing.

![Sequence Diagram for User Scenario 15](WasteWise_Reports/problem_description/images/sequence_diagram_US15.drawio.png)  
**Figure 21:** Sequence Diagram for User Scenario 15

**Scenario 16:** A Private Owner views the profile of another Private Owner.  
**Background:** Company G, who is looking for a Waste Processing Facility to collaborate with, has found the profile of Company H, who operates in their region. Thus, they would like to view relevant company information regarding Company H.  
**Sequence Diagram:** The following diagram displays the flow of this scenario. It clearly displays the steps that are to be followed for a Private Owner to view the profile of another Private Owner within WasteWise system.

![Sequence Diagram for User Scenario 16](WasteWise_Reports/problem_description/images/sequence_diagram_US16.drawio.png)  
**Figure 22:** Sequence Diagram for User Scenario 16

**Scenario 17:** A Governmental User can view the forms sent to them.  
**Background:** Becky works in a Dutch municipality, and she has received several complaint forms from civilians. She would like to view the forms that were assigned to her.  
**Sequence Diagram:** The following diagram displays the flow of this scenario. It clearly displays the steps that are to be followed for a Governmental User to view the forms sent to them in their profile within WasteWise.

![Sequence Diagram for User Scenario 17](WasteWise_Reports/problem_description/images/sequence_diagram_US17.drawio.png)  
**Figure 23:** Sequence Diagram for User Scenario 17

<!-- TOC --><a name="appendix-c-acknowledgments"></a>

## Appendix C: Acknowledgments

<!-- TOC --><a name="wildfires-project"></a>

### Wildfires Project

For the layout of the report, we have taken inspiration from the [example project on Wildfires](https://gitlab.ewi.tudelft.nl/cs4505/2024-2025/desosa2024x/-/tree/main/content/projects/example-wild-fires?ref_type=heads) that has been made available in the common GitLab.
