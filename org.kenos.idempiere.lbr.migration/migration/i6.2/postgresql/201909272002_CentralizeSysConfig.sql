UPDATE AD_SysConfig SET Name='LBR_NFSE_MOGI_PASS' WHERE Name='KENOSERP_PASSWORD_LOGIN_NFSE_MOGI'
;
UPDATE AD_SysConfig SET Name='LBR_NFSE_MOGI_USER' WHERE Name='KENOSERP_USER_LOGIN_NFSE_MOGI'
;
UPDATE AD_SysConfig SET Name='LBR_SISCOMEX_PRODUCT_REGEX' WHERE Name='KNS_SISCOMEX_PRODUCT_REGEX'
;
UPDATE AD_SysConfig SET Name='LBR_VOLUME' WHERE Name='VOLUME'
;
UPDATE AD_SysConfig SET Name='LBR_ICMS_MATRIX_ENABLED' WHERE Name='Z_USE_ICMS_MATRIX'
;
UPDATE AD_SysConfig SET Name='LBR_ISS_MATRIX_ENABLED' WHERE Name='Z_USE_ISS_MATRIX'
;
UPDATE AD_SysConfig SET Name='LBR_AUTOMATIC_ADJUST_MVA' WHERE Name='LBR_AUTOMATOC_ADJUST_IVA'
;
SELECT Register_Migration_Script ('201909272002_CentralizeSysConfig.sql') FROM DUAL
;
