//
// Created by Alp Sahin on 19.06.2024.
//

import Foundation
import UIKit
import PayFortSDK

class CustomPaymentViewController: UIViewController {
    let cardNumberView = CardNumberView()
    let expiryDateView = ExpiryDateView()
    let cvcNumberView = CVCNumberView()
    let holderNameView = HolderNameView()
    let payButton = PayButton()

    let headerLabel = UILabel()
    let closeButton = UIButton()
    let iconImageView = UIImageView()
    let headerLabel2 = UILabel()
    let additionalIconsStack = UIStackView()
    let saveCardLabel = UILabel()
    let saveCardSwitch = UISwitch()
    let setDefaultLabel = UILabel()
    let setDefaultSwitch = UISwitch()

    var onSuccess: ([String: String], [String: String]) -> Void?
    var onFaild: ([String: String], [String: String], String) -> Void?
    var onCancel: () -> Void?
    var environment: PayFortEnviroment
    var request : [String: String]

    init(onSuccess: @escaping ([String: String], [String: String]) -> Void, onFaild: @escaping ([String: String], [String: String], String) -> Void, onCancel: @escaping () -> Void, request: [String: String], environment: PayFortEnviroment) {
        self.onSuccess = onSuccess
        self.onFaild = onFaild
        self.onCancel = onCancel
        self.request = request
        self.environment = environment
        super.init(nibName: nil, bundle: nil)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        setupViews()
        setupLayout()
    }

    private func setupViews() {
        // Header and close button
        headerLabel.text = "Header Text"
        headerLabel.textColor = .black
        closeButton.setImage(UIImage(named: "close_icon"), for: .normal)
        closeButton.addTarget(self, action: #selector(closeButtonTapped), for: .touchUpInside)

        // Additional icons setup
        iconImageView.image = UIImage(named: "icon_image")
        headerLabel2.text = "Header Label"
        headerLabel2.textColor = .gray

        additionalIconsStack.axis = .horizontal
        additionalIconsStack.spacing = 8
        for _ in 0..<3 { // Assuming 3 icons for simplicity
            let icon = UIImageView(image: UIImage(named: "additional_icon"))
            additionalIconsStack.addArrangedSubview(icon)
        }

        // Save card and set default switches
        saveCardLabel.text = "Save Card"
        saveCardLabel.textColor = .black
        setDefaultLabel.text = "Set as Default"
        setDefaultLabel.textColor = .black

        configureTextFieldsOfUIView(cardNumberView, placeholder: "Card Number")
        configureTextFieldsOfUIView(holderNameView, placeholder: "Card Holder")
        configureTextFieldsOfUIView(expiryDateView, placeholder: "Expiry Date")
        configureTextFieldsOfUIView(cvcNumberView, placeholder: "CVV")

        let builder = PayComponents(cardNumberView: cardNumberView, expiryDateView: expiryDateView, cvcNumberView: cvcNumberView, holderNameView: holderNameView, rememberMe: true, language: "en")
        payButton.setup(with: request, enviroment: environment, payButtonBuilder: builder, viewController: self) {
            print("process started")
        } success: { (requestDic, responeDic) in
            print("succeeded: - \(requestDic) - \(responeDic)")
            self.onSuccess(requestDic, responeDic)
            self.dismiss(animated: true)
            return
        } faild: { (requestDic, responeDic, message) in
            print("failed: \(message) - \(requestDic) - \(responeDic)")
            self.onFaild(requestDic, responeDic, message)
            self.dismiss(animated: true)
            return
        }

        payButton.setTitle("Add Card", for: .normal)
        payButton.backgroundColor = .blue
        payButton.layer.cornerRadius = 6
    }

    private func configureTextFieldsOfUIView(_ uiView: UIView, placeholder: String) {
        for subview in view.subviews {
            if let textField = subview as? UITextField {
                configureTextField(textField, placeholder: placeholder)
            } else {
                // Recursively apply to subviews
                configureTextFieldsOfUIView(subview, placeholder: placeholder)
            }
        }
    }

    private func configureTextField(_ textField: UITextField, placeholder: String) {
        textField.placeholder = placeholder
        textField.font = UIFont.systemFont(ofSize: 15, weight: .regular)
        textField.borderStyle = .roundedRect
        textField.layer.borderWidth = 1
        textField.layer.cornerRadius = 6
        textField.layer.borderColor = UIColor.gray.cgColor
        textField.setPadding(left: 16)
    }


    private func setupLayout() {
        let expiryAndCvvStackView = UIStackView(arrangedSubviews: [expiryDateView, cvcNumberView])
        expiryAndCvvStackView.axis = .horizontal
        expiryAndCvvStackView.distribution = .fillEqually
        expiryAndCvvStackView.spacing = 16

        // Layout constraints
        headerLabel.translatesAutoresizingMaskIntoConstraints = false
        closeButton.translatesAutoresizingMaskIntoConstraints = false
        iconImageView.translatesAutoresizingMaskIntoConstraints = false
        headerLabel2.translatesAutoresizingMaskIntoConstraints = false
        additionalIconsStack.translatesAutoresizingMaskIntoConstraints = false
        cardNumberView.translatesAutoresizingMaskIntoConstraints = false
        holderNameView.translatesAutoresizingMaskIntoConstraints = false
        expiryDateView.translatesAutoresizingMaskIntoConstraints = false
        cvcNumberView.translatesAutoresizingMaskIntoConstraints = false
        expiryAndCvvStackView.translatesAutoresizingMaskIntoConstraints = false
        saveCardLabel.translatesAutoresizingMaskIntoConstraints = false
        saveCardSwitch.translatesAutoresizingMaskIntoConstraints = false
        setDefaultLabel.translatesAutoresizingMaskIntoConstraints = false
        setDefaultSwitch.translatesAutoresizingMaskIntoConstraints = false
        payButton.translatesAutoresizingMaskIntoConstraints = false

        // Add subviews
        view.addSubview(headerLabel)
        view.addSubview(closeButton)
        view.addSubview(iconImageView)
        view.addSubview(headerLabel2)
        view.addSubview(additionalIconsStack)
        view.addSubview(cardNumberView)
        view.addSubview(holderNameView)
        view.addSubview(expiryAndCvvStackView)
        view.addSubview(saveCardLabel)
        view.addSubview(saveCardSwitch)
        view.addSubview(setDefaultLabel)
        view.addSubview(setDefaultSwitch)
        view.addSubview(payButton)

        NSLayoutConstraint.activate([
            // Header and close button
            headerLabel.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16),
            headerLabel.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            closeButton.centerYAnchor.constraint(equalTo: headerLabel.centerYAnchor),
            closeButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            closeButton.widthAnchor.constraint(equalToConstant: 20),
            closeButton.heightAnchor.constraint(equalToConstant: 20),

            // Icon and header label 2
            iconImageView.topAnchor.constraint(equalTo: headerLabel.bottomAnchor, constant: 12),
            iconImageView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            iconImageView.widthAnchor.constraint(equalToConstant: 14),
            iconImageView.heightAnchor.constraint(equalToConstant: 14),
            headerLabel2.centerYAnchor.constraint(equalTo: iconImageView.centerYAnchor),
            headerLabel2.leadingAnchor.constraint(equalTo: iconImageView.trailingAnchor, constant: 8),

            // Additional icons stack
            additionalIconsStack.centerYAnchor.constraint(equalTo: iconImageView.centerYAnchor),
            additionalIconsStack.leadingAnchor.constraint(equalTo: headerLabel2.trailingAnchor, constant: 8),
            additionalIconsStack.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),

            // Text fields
            cardNumberView.topAnchor.constraint(equalTo: headerLabel2.bottomAnchor, constant: 16),
            cardNumberView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            cardNumberView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            cardNumberView.heightAnchor.constraint(equalToConstant: 44),

            holderNameView.topAnchor.constraint(equalTo: cardNumberView.bottomAnchor, constant: 16),
            holderNameView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            holderNameView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            holderNameView.heightAnchor.constraint(equalToConstant: 44),

            // Expiry Date and CVV Stack View
            expiryAndCvvStackView.topAnchor.constraint(equalTo: holderNameView.bottomAnchor, constant: 16),
            expiryAndCvvStackView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            expiryAndCvvStackView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            expiryAndCvvStackView.heightAnchor.constraint(equalToConstant: 44),

            // Save card switch
            saveCardLabel.topAnchor.constraint(equalTo: expiryAndCvvStackView.bottomAnchor, constant: 16),
            saveCardLabel.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            saveCardSwitch.centerYAnchor.constraint(equalTo: saveCardLabel.centerYAnchor),
            saveCardSwitch.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),

            // Set default switch
            setDefaultLabel.topAnchor.constraint(equalTo: saveCardLabel.bottomAnchor, constant: 24),
            setDefaultLabel.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            setDefaultSwitch.centerYAnchor.constraint(equalTo: setDefaultLabel.centerYAnchor),
            setDefaultSwitch.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),

            // Add button
            payButton.topAnchor.constraint(equalTo: setDefaultLabel.bottomAnchor, constant: 24),
            payButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            payButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            payButton.heightAnchor.constraint(equalToConstant: 44)
        ])
    }

    @objc private func closeButtonTapped() {
        self.dismiss(animated: true, completion: nil)
        onCancel()
    }
}

extension UITextField {
    func setPadding(left: CGFloat) {
        let paddingView = UIView(frame: CGRect(x: 0, y: 0, width: left, height: self.frame.height))
        self.leftView = paddingView
        self.leftViewMode = .always
    }
}
