class PayFortCardPageData {
  final String headerText;
  final String headerIconUrl;
  final String secondaryHeaderText;
  final List<String> iconUrls;
  final int headerTextColor;
  final int secondaryHeaderTextColor;
  final int toggleButtonActiveColor;
  final int toggleButtonInactiveColor;
  final String cardNumberLabel;
  final String cardHolderLabel;
  final String expiryDateLabel;
  final String cvvLabel;
  final int textFieldHintColor;
  final String saveCardDataLabel;
  final String setCardDefaultLabel;
  final String addCardButtonLabel;
  final int addCardButtonBackgroundColor;

  PayFortCardPageData({
    required this.headerText,
    required this.headerIconUrl,
    required this.secondaryHeaderText,
    required this.iconUrls,
    required this.headerTextColor,
    required this.secondaryHeaderTextColor,
    required this.toggleButtonActiveColor,
    required this.toggleButtonInactiveColor,
    required this.cardNumberLabel,
    required this.cardHolderLabel,
    required this.expiryDateLabel,
    required this.cvvLabel,
    required this.textFieldHintColor,
    required this.saveCardDataLabel,
    required this.setCardDefaultLabel,
    required this.addCardButtonLabel,
    required this.addCardButtonBackgroundColor,
  });

  Map<String, dynamic> asMap() {
    return {
      'headerText': headerText,
      'headerIconUrl': headerIconUrl,
      'secondaryHeaderText': secondaryHeaderText,
      'iconUrls': iconUrls,
      'headerTextColor': headerTextColor,
      'secondaryHeaderTextColor': secondaryHeaderTextColor,
      'toggleButtonActiveColor': toggleButtonActiveColor,
      'toggleButtonInactiveColor': toggleButtonInactiveColor,
      'cardNumberLabel': cardNumberLabel,
      'cardHolderLabel': cardHolderLabel,
      'expiryDateLabel': expiryDateLabel,
      'cvvLabel': cvvLabel,
      'textFieldHintColor': textFieldHintColor,
      'saveCardDataLabel': saveCardDataLabel,
      'setCardDefaultLabel': setCardDefaultLabel,
      'addCardButtonLabel': addCardButtonLabel,
    };
  }
}
