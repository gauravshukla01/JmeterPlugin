Feature: Cucumber Test Scenarios

Background: 
Given A workbook named "TestData8" and sheet named "Data" is read

@GooglePerformance
Scenario Outline: TC_01																							
		Given I provide the following data for HTTPSampler and LoopController:<URI>,<port>,<protocol>,<path>,<method>,<sName>,<body>,<loops>,<setfirst>,<tName>,<tCount>,<ramp> 
		When I create the TestPlan<testPlan> with input data and Run the Test
  	Then Report should be created

Examples: 
      | URI | port | protocol | path | method | sName | body | loops | setfirst | tName | tCount | ramp | testPlan |
      | URL | Port | Protocol | Path | Method | SamplerName | Body | LoopCount | setFirst | ThreadName | ThreadCount | Rampup | Testplan | 
