package hu.ps.common.exception;
/*
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;

import hu.ps.model.exception.GlobalErrorCode;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import hu.ps.common.validator.ValidatorResultGlobalErrorCode;
import hu.ps.model.dto.error.ErrorResponse;
import hu.ps.model.dto.error.ValidationError;
import hu.ps.model.dto.error.ValidationErrorResponse;
import hu.ps.model.exception.GlobalError;
import hu.ps.model.exception.GlobalErrorCode.Global;
import hu.ps.model.exception.GlobalException;
import hu.ps.model.exception.GlobalRuntimeException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ControllerResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	private ResponseEntity<Object> createResponseEntity(BindException e) {
		var exception = new GlobalException(e.getMessage(), Global.VALIDATION_ERROR, e);
		log.info(exception.getMessage());
		var response = new ValidationErrorResponse(exception.getErrorEventId(), exception.getErrorTimestamp(),
				exception.getErrorCode(), exception.getErrorMessage(), exception.getHttpStatusCode());
		e.getAllErrors().forEach(error -> {
			if (error instanceof FieldError) {
				var fieldError = (FieldError) error;
				log.debug(fieldError.getCodes()[0]);
				response.getValidationErrors().add(new ValidationError(fieldError.getObjectName(),
						fieldError.getField(), fieldError.getCodes(), fieldError.getDefaultMessage()));
			} else {
				response.getValidationErrors()
						.add(new ValidationError(error.getObjectName(), error.getCodes(), error.getDefaultMessage()));
			}
		});
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpStatusCode()));
	}

	@ExceptionHandler(value = { GlobalException.class, GlobalRuntimeException.class })
	ResponseEntity<ErrorResponse> handleGlobalException(Exception e) {
		if (e instanceof GlobalError) {
			log.warn(e.getMessage());
			ErrorResponse response;
			if (e instanceof GlobalRuntimeException) {
				var er = (GlobalError) e;
				response = new ErrorResponse(er.getErrorEventId(), er.getErrorTimestamp(), er.getErrorCode(),
						(e.getMessage() != null ? e.getMessage() : er.getErrorMessage()), er.getHttpStatusCode());
			} else {
				response = new ErrorResponse((GlobalError) e);
			}
			return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpStatusCode()));
		}

		return handleException(e);
	}

	@ExceptionHandler(value = { ResponseStatusException.class })
	ResponseEntity<ErrorResponse> handleGlobalException(ResponseStatusException e) {
		var msgError = (e.getReason() == null || e.getReason().length() == 0) ? e.getMessage() : e.getReason();
		var errorCode = e.getStatus().is4xxClientError() ? Global.BAD_REQUEST : Global.BELSO_HIBA;
		var exception = new GlobalException(msgError, errorCode, e);
		log.warn(exception.getMessage());
		var response = new ErrorResponse(exception.getErrorEventId(), exception.getErrorTimestamp(),
				exception.getErrorCode(), msgError, e.getStatus().value());
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpStatusCode()));
	}

	@ExceptionHandler(value = { AccessDeniedException.class })
	ResponseEntity<ErrorResponse> handleGlobalException(AccessDeniedException e) {
		var exception = new GlobalException(e.getMessage(), Global.ACCESS_DENIED, e);
		log.warn(exception.getMessage());
		log.debug(exception.getMessage(), e);
		var response = new ErrorResponse(exception.getErrorEventId(), exception.getErrorTimestamp(),
				exception.getErrorCode(), exception.getErrorMessage(), exception.getHttpStatusCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpStatusCode()));
	}

	@ExceptionHandler(value = { ValidationException.class })
	ResponseEntity<ErrorResponse> handleGlobalException(ValidationException e) {
		var exception = new GlobalException(e.getMessage(), Global.BAD_REQUEST, e);
		log.debug(exception.getMessage(), e);
		var response = new ErrorResponse(exception.getErrorEventId(), exception.getErrorTimestamp(),
				exception.getErrorCode(), exception.getErrorMessage(), exception.getHttpStatusCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpStatusCode()));
	}

	@ExceptionHandler(value = { ConstraintViolationException.class })
	ResponseEntity<ErrorResponse> handleGlobalException(ConstraintViolationException e) {
		for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
			ValidatorResultGlobalErrorCode GlobalErrorCodeValidatorResult = AnnotationUtils.findAnnotation(
					constraintViolation.getConstraintDescriptor().getAnnotation().getClass(),
					ValidatorResultGlobalErrorCode.class);
			if (GlobalErrorCodeValidatorResult != null) {
				var exception = new GlobalException(e.getMessage(), GlobalErrorCodeValidatorResult.GlobalErrorCode(), e);
				var response = new ErrorResponse(exception.getErrorEventId(), exception.getErrorTimestamp(),
						exception.getErrorCode(), exception.getErrorMessage(), exception.getHttpStatusCode());
				return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpStatusCode()));
			}
		}
		var exception = new GlobalException(e.getMessage(), Global.VALIDATION_ERROR, e);
		log.info(exception.getMessage());
		var response = new ValidationErrorResponse(exception.getErrorEventId(), exception.getErrorTimestamp(),
				exception.getErrorCode(), exception.getErrorMessage(), exception.getHttpStatusCode());
		e.getConstraintViolations().forEach(error ->
			response.getValidationErrors().add(new ValidationError(error.getPropertyPath().iterator().next().toString(),
					new String[] { error.getPropertyPath().toString() }, error.getMessage()))
		);
		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpStatusCode()));
	}

	@Override
	protected ResponseEntity<Object> handleBindException(BindException e, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		return createResponseEntity(e);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return createResponseEntity(e);
	}

	@ExceptionHandler
	ResponseEntity<ErrorResponse> handleException(Throwable e) {
		var exception = new GlobalException(e);
		log.error(exception.getMessage(), exception);
		var response = new ErrorResponse(exception);

		return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpStatusCode()));
	}

}
*/
