SET SQLBLANKLINES ON
SET DEFINE OFF

-- Correção no script RV_LBR_RequestDetails
-- 09/01/2019 15h14min41s BRST

DELETE FROM AD_Sequence WHERE AD_Sequence_UU='8a48c023-589f-4e3c-8205-09e0f5e1c5b9';

SELECT Register_Migration_Script ('201912091531_Fix_RV_LBR_RequestDetails.sql') FROM DUAL
;
